package kr.sul.servercore.nbtapi

import net.minecraft.server.v1_12_R1.NBTTagCompound
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.Consumer

// [해결] NbtItem.item을 통해 수정한 것들이 무시되는 문제 해결(nmsItem tag만 applyToOriginal() 때 적용이 되고 있었음) e.g. lore 수정
class NbtItem(val item: ItemStack) {
    private val itemCopy = item.clone()
    private val nmsItem = CraftItemStack.asNMSCopy(item)
    val tag = TagWrapper(if (nmsItem.hasTag()) nmsItem.tag!! else NBTTagCompound())
    fun applyToOriginal() {
        val realTag = tag.realTag
        nmsItem.tag = realTag
        val difference = ItemDifference(item.itemMeta, itemCopy.itemMeta, tag.unbreakableModifiedViaNMS)
        val asBukkitItem = CraftItemStack.asBukkitCopy(nmsItem)
        // item(Bukkit)을 참조해서 Meta를 수정한 사항이 있다면, nmsItem 에도 패치를 해 줌
        if (difference.hasDifference()) {
            difference.apply(asBukkitItem)
        }
        item.itemMeta = asBukkitItem.itemMeta
    }


    // unbreakable이 Bukkit과 Tag를 통해 두가지 방법을 통해 수정이 가능함에 따라 우선순위를 매기기 위해 도입
    class TagWrapper(val realTag: NBTTagCompound): NBTTagCompound() {
        var unbreakableModifiedViaNMS = false
        override fun setBoolean(s: String?, flag: Boolean) {
            if (s == "Unbreakable") {
                unbreakableModifiedViaNMS = true
            }
            super.setBoolean(s, flag)
        }
    }


    // item과 itemCopy의 차이점을 저장한 후 apply로 차이점만 덮어쓰기
    private class ItemDifference(newMeta: ItemMeta, originMeta: ItemMeta, unbreakableModifiedViaNMS: Boolean) {
        val differenceList = arrayListOf<Consumer<ItemMeta>>()
        init {
            if (newMeta != originMeta) {
                if (newMeta.lore != originMeta.lore) {
                    differenceList.add { conveyedMeta -> conveyedMeta.lore = newMeta.lore }
                }
                if (newMeta.displayName != originMeta.displayName) {
                    differenceList.add { conveyedMeta -> conveyedMeta.displayName = newMeta.displayName }
                }
                if (newMeta.enchants != originMeta.enchants) {
                    differenceList.add { conveyedMeta ->
                        for (enchantToRemove in conveyedMeta.enchants.keys) {
                            conveyedMeta.removeEnchant(enchantToRemove)
                        }
                        for ((enchantToAdd, level) in newMeta.enchants) {
                            conveyedMeta.addEnchant(enchantToAdd, level, true)
                        }
                    }
                }
                if (newMeta.isUnbreakable != originMeta.isUnbreakable) {
                    // NMS를 통해서 unbreakable이 수정되지 않은 경우에만 isUnbreakable을 Bukkit의 것으로 덮어씀
                    if (!unbreakableModifiedViaNMS) {
                        differenceList.add { conveyedMeta ->
                            conveyedMeta.isUnbreakable = newMeta.isUnbreakable
                        }
                    }
                }
                if (newMeta.itemFlags != originMeta.itemFlags) {
                    differenceList.add { conveyedMeta ->
                        for (flagToRemove in conveyedMeta.itemFlags) {
                            conveyedMeta.removeItemFlags(flagToRemove)
                        }
                        for (flagToAdd in newMeta.itemFlags) {
                            conveyedMeta.addItemFlags(flagToAdd)
                        }
                    }
                }
                if (newMeta.localizedName != originMeta.localizedName) {
                    differenceList.add { conveyedMeta ->
                        conveyedMeta.localizedName = newMeta.localizedName
                    }
                }
            }
        }
        fun hasDifference(): Boolean {
            return differenceList.size > 0
        }
        // 저장된 차이점을 아이템에 적용
        fun apply(item: ItemStack) {
            val meta = item.itemMeta
            for (difference in differenceList) {
                difference.accept(meta)
            }
            item.itemMeta = meta
        }
    }
}
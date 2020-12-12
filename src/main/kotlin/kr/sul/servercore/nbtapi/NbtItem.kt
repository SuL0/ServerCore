package kr.sul.servercore.nbtapi

import net.minecraft.server.v1_12_R1.NBTTagCompound
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

class NbtItem(private val item: ItemStack) {
    private val nmsItem = CraftItemStack.asNMSCopy(item)
    val tag = if (nmsItem.hasTag()) nmsItem.tag!! else NBTTagCompound()

    fun applyToOriginal() {
        nmsItem.tag = tag
        item.itemMeta = CraftItemStack.asBukkitCopy(nmsItem).itemMeta
    }
}
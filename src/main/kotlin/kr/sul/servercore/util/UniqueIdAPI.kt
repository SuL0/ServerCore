package kr.sul.servercore.util

import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import java.util.*

object UniqueIdAPI {
    private const val UNIQUE_ID_KEY = "Item-UniqueID"

    @JvmStatic
    fun hasUniqueID(item: ItemStack): Boolean {
        if (item.type == Material.AIR) return false
        if (item !is CraftItemStack || item.handle == null) return false
        val nmsItem = item.handle
        return nmsItem.tagOrDefault.hasKey(UNIQUE_ID_KEY)
    }

    @JvmStatic
    fun getUniqueID(item: ItemStack): String? {
        if (item !is CraftItemStack || item.handle == null) return null
        val nmsItem = item.handle
        val uniqueId = nmsItem.tagOrDefault.getString(UNIQUE_ID_KEY)
        if (uniqueId == "") {
            return null
        }
        return uniqueId
    }



    @JvmStatic
    fun carveUniqueID(item: ItemStack) {
        carveSpecificUniqueID(item, UUID.randomUUID())
    }

    @JvmStatic
    fun carveSpecificUniqueID(item: ItemStack, uuid: UUID) {
        if (hasUniqueID(item)) throw Exception()
        val nmsItem = (item as CraftItemStack).handle
        nmsItem.tagOrDefault.setString(UNIQUE_ID_KEY, uuid.toString())
    }

    @JvmStatic
    fun wipeAndCarveNewUniqueID(item: ItemStack) {
        if (!hasUniqueID(item)) throw Exception()
        val nmsItem = (item as CraftItemStack).handle
        nmsItem.tagOrDefault.remove(UNIQUE_ID_KEY)
        carveUniqueID(item)
    }

    @JvmStatic
    fun wipeUniqueID(item: ItemStack) {
        if (!hasUniqueID(item)) throw Exception()
        val nmsItem = (item as CraftItemStack).handle
        nmsItem.tagOrDefault.remove(UNIQUE_ID_KEY)
    }
}
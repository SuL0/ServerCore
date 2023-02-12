package kr.sul.servercore.util

import kr.sul.minecraft.ItemUniqueID
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import java.util.*

object UniqueIdAPI {

    @JvmStatic
    fun hasUniqueID(item: ItemStack): Boolean {
        if (checkIfItemHasHandle(item)) return false
        val nmsItem = (item as CraftItemStack).handle
        return ItemUniqueID.hasUniqueID(nmsItem)
    }

    @JvmStatic
    fun getUniqueID(item: ItemStack): String? {
        if (checkIfItemHasHandle(item)) return null
        val nmsItem = (item as CraftItemStack).handle
        return ItemUniqueID.getUniqueID(nmsItem)
    }



    @JvmStatic
    fun carveUniqueID(item: ItemStack) {
        carveSpecificUniqueID(item, UUID.randomUUID())
    }

    @JvmStatic
    fun carveSpecificUniqueID(item: ItemStack, uuid: UUID) {
        if (checkIfItemHasHandle(item)) throw Exception()
        val nmsItem = (item as CraftItemStack).handle
        ItemUniqueID.carveSpecificUniqueID(nmsItem, uuid)
    }

    @JvmStatic
    fun wipeAndCarveNewUniqueID(item: ItemStack) {
        if (checkIfItemHasHandle(item)) throw Exception()
        val nmsItem = (item as CraftItemStack).handle
        ItemUniqueID.wipeAndCarveNewUniqueID(nmsItem)
    }

    @JvmStatic
    fun wipeUniqueID(item: ItemStack) {
        if (checkIfItemHasHandle(item)) throw Exception()
        val nmsItem = (item as CraftItemStack).handle
        ItemUniqueID.wipeUniqueID(nmsItem)
    }

    private fun checkIfItemHasHandle(item: ItemStack): Boolean {
        if (item.type == Material.AIR) return false
        if (item !is CraftItemStack || item.handle == null) return false
        return true
    }
}
package kr.sul.servercore.util

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

object UniqueIdAPI {
    private const val UNIQUE_ID_KEY = "Item-UniqueID"

    @JvmStatic
    fun carveUniqueID(item: ItemStack) {
        if (hasUniqueID(item)) throw Exception()
        val nbti = NBTItem(item)
        nbti.setString(UNIQUE_ID_KEY, UUID.randomUUID().toString())
        item.itemMeta = nbti.item.itemMeta
    }

    @JvmStatic
    fun hasUniqueID(item: ItemStack): Boolean {
        if (item.type == Material.AIR) return false
        val nbti = NBTItem(item)
        return nbti.hasKey(UNIQUE_ID_KEY)
    }

    @JvmStatic
    fun getUniqueID(item: ItemStack): String {
        val nbti = NBTItem(item)
        return nbti.getString(UNIQUE_ID_KEY) ?: throw Exception()
    }
}
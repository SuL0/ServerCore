package kr.sul.servercore.util

import kr.sul.servercore.nbtapi.NbtItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

object UniqueIdAPI {
    private const val UNIQUE_ID_KEY = "Item-UniqueID"

    @JvmStatic
    fun carveUniqueID(item: ItemStack) {
        if (hasUniqueID(item)) throw Exception()
        val nbti = NbtItem(item)
        nbti.tag.setString(UNIQUE_ID_KEY, UUID.randomUUID().toString())
        nbti.applyToOriginal()
    }

    @JvmStatic
    fun hasUniqueID(item: ItemStack): Boolean {
        if (item.type == Material.AIR) return false
        val nbti = NbtItem(item)
        return nbti.tag.hasKey(UNIQUE_ID_KEY)
    }

    @JvmStatic
    fun getUniqueID(item: ItemStack): String {
        val nbti = NbtItem(item)
        if (!nbti.tag.hasKey(UNIQUE_ID_KEY)) throw Exception()
        return nbti.tag.getString(UNIQUE_ID_KEY)
    }
}
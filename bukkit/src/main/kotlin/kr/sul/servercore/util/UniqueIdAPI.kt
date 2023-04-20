package kr.sul.servercore.util

import kr.sul.servercore.ServerCore.Companion.plugin
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

object UniqueIdAPI {
    private const val UNIQUE_ID_KEY = "Item-UniqueID"
    private val uniqueIdNamespaceKey = NamespacedKey.fromString(UNIQUE_ID_KEY, plugin)!!

    @JvmStatic
    fun hasUniqueID(item: ItemStack): Boolean {
        if (item.type == Material.AIR) return false
        return item.itemMeta.persistentDataContainer.has(uniqueIdNamespaceKey)
    }

    @JvmStatic
    fun getUniqueID(item: ItemStack): String? {
        val uniqueId = item.itemMeta.persistentDataContainer
            .getOrDefault(uniqueIdNamespaceKey, PersistentDataType.STRING, "")
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
        val meta = item.itemMeta
        meta.persistentDataContainer
            .set(uniqueIdNamespaceKey, PersistentDataType.STRING, uuid.toString())
        item.setItemMeta(meta)
    }

    @JvmStatic
    fun wipeAndCarveNewUniqueID(item: ItemStack) {
        if (!hasUniqueID(item)) throw Exception()
        wipeUniqueID(item)
        carveUniqueID(item)
    }

    @JvmStatic
    fun wipeUniqueID(item: ItemStack) {
        if (!hasUniqueID(item)) throw Exception()
        val meta = item.itemMeta
        meta.persistentDataContainer.remove(uniqueIdNamespaceKey)
        item.setItemMeta(meta)
    }
}
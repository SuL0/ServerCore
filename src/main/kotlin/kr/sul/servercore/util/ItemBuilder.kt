package kr.sul.servercore.util

import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.material.MaterialData
import java.util.*
import java.util.function.Consumer

object ItemBuilder {
    fun ItemStack.amountIB(amount: Int): ItemStack {
        setAmount(amount)
        return this
    }

    fun ItemStack.nameIB(name: String): ItemStack {
        val meta = itemMeta
        meta.displayName = name.c()
        itemMeta = meta
        return this
    }

    fun ItemStack.loreIB(text: String): ItemStack {
        val meta = itemMeta
        var lore: MutableList<String>? = meta.lore
        if (lore == null) lore = ArrayList()
        lore.add(text.c())
        meta.lore = lore
        itemMeta = meta
        return this
    }

    fun ItemStack.loreIB(vararg text: String): ItemStack {
        Arrays.stream(text).forEach { this.loreIB(it) }
        return this
    }

    fun ItemStack.loreIB(text: List<String>): ItemStack {
        text.forEach { this.loreIB(it) }
        return this
    }

    fun ItemStack.durabilityIB(durability: Int): ItemStack {
        setDurability(durability.toShort())
        return this
    }

    fun ItemStack.dataIB(data: Int): ItemStack {
        setData(MaterialData(type, data.toByte()))
        return this
    }

    fun ItemStack.enchantmentIB(enchantment: Enchantment, level: Int): ItemStack {
        addUnsafeEnchantment(enchantment, level)
        return this
    }

    fun ItemStack.enchantmentIB(enchantment: Enchantment): ItemStack {
        addUnsafeEnchantment(enchantment, 1)
        return this
    }

    fun ItemStack.typeIB(material: Material): ItemStack {
        type = material
        return this
    }

    fun ItemStack.clearLoreIB(): ItemStack {
        val meta = itemMeta
        meta.lore = ArrayList()
        itemMeta = meta
        return this
    }

    fun ItemStack.clearEnchantmentsIB(): ItemStack {
        enchantments.keys.forEach(Consumer<Enchantment> { this.removeEnchantment(it) })
        return this
    }

    fun ItemStack.colorIB(color: Color): ItemStack {
        if (type == Material.LEATHER_BOOTS
                || type == Material.LEATHER_CHESTPLATE
                || type == Material.LEATHER_HELMET
                || type == Material.LEATHER_LEGGINGS) {

            val meta = itemMeta as LeatherArmorMeta
            meta.color = color
            itemMeta = meta
            return this
        } else {
            throw IllegalArgumentException("Colors only applicable for leather armor!")
        }
    }

    fun ItemStack.flagIB(vararg flag: ItemFlag): ItemStack {
        val meta = itemMeta
        meta.addItemFlags(*flag)
        itemMeta = meta
        return this
    }

    fun ItemStack.unbreakableIB(b: Boolean): ItemStack {
        val meta = itemMeta
        meta.isUnbreakable = b
        itemMeta = meta
        return this
    }

    private fun String.c(): String {
        return ChatColor.translateAlternateColorCodes('&', this)
    }

}
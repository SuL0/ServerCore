package kr.sul.servercore.util

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.SkullType
import org.bukkit.block.Block
import org.bukkit.block.Skull
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.net.URI
import java.net.URISyntaxException
import java.util.*


/**
 * A library for the Bukkit API to create player skulls
 * from names, base64 strings, and texture URLs.
 *
 *
 * Does not use any NMS code, and should work across all versions.
 *
 * @author Dean B on 12/28/2016.
 */
object SkullCreator {
    private var warningPosted = false

    // some reflection stuff to be used when setting a skull's profile
    private var blockProfileField: Field? = null
    private var metaSetProfileMethod: Method? = null
    private var metaProfileField: Field? = null

    /**
     * Creates a player skull, should work in both legacy and new Bukkit APIs.
     */
    fun createSkull(): ItemStack {
        checkLegacy()
        return try {
            ItemStack(Material.valueOf("PLAYER_HEAD"))
        } catch (e: IllegalArgumentException) {
            ItemStack(Material.valueOf("SKULL_ITEM"), 1, 3.toShort())
        }
    }

    /**
     * Creates a player skull item with the skin based on a player's name.
     *
     * @param name The Player's name.
     * @return The head of the Player.
     */
    @Deprecated("names don't make for good identifiers.")
    fun itemFromName(name: String?): ItemStack {
        return itemWithName(createSkull(), name)
    }

    /**
     * Creates a player skull item with the skin based on a player's UUID.
     *
     * @param id The Player's UUID.
     * @return The head of the Player.
     */
    fun itemFromUuid(id: UUID?): ItemStack {
        return itemWithUuid(createSkull(), id)
    }

    /**
     * Creates a player skull item with the skin at a Mojang URL.
     *
     * @param url The Mojang URL.
     * @return The head of the Player.
     */
    fun itemFromUrl(url: String?): ItemStack? {
        return itemWithUrl(createSkull(), url)
    }

    /**
     * Creates a player skull item with the skin based on a base64 string.
     *
     * @param base64 The Mojang URL.
     * @return The head of the Player.
     */
    fun itemFromBase64(base64: String): ItemStack? {
        return itemWithBase64(createSkull(), base64)
    }

    /**
     * Modifies a skull to use the skin of the player with a given name.
     *
     * @param item The item to apply the name to. Must be a player skull.
     * @param name The Player's name.
     * @return The head of the Player.
     */
    @Deprecated("names don't make for good identifiers.")
    fun itemWithName(item: ItemStack, name: String?): ItemStack {
        notNull(item, "item")
        notNull(name, "name")
        val meta = item.itemMeta as SkullMeta
        meta.owner = name
        item.itemMeta = meta
        return item
    }

    /**
     * Modifies a skull to use the skin of the player with a given UUID.
     *
     * @param item The item to apply the name to. Must be a player skull.
     * @param id   The Player's UUID.
     * @return The head of the Player.
     */
    fun itemWithUuid(item: ItemStack, id: UUID?): ItemStack {
        notNull(item, "item")
        notNull(id, "id")
        val meta = item.itemMeta as SkullMeta
        meta.owningPlayer = Bukkit.getOfflinePlayer(id!!)
        item.itemMeta = meta
        return item
    }

    /**
     * Modifies a skull to use the skin at the given Mojang URL.
     *
     * @param item The item to apply the skin to. Must be a player skull.
     * @param url  The URL of the Mojang skin.
     * @return The head associated with the URL.
     */
    fun itemWithUrl(item: ItemStack, url: String?): ItemStack? {
        notNull(item, "item")
        notNull(url, "url")
        return itemWithBase64(item, urlToBase64(url))
    }

    /**
     * Modifies a skull to use the skin based on the given base64 string.
     *
     * @param item   The ItemStack to put the base64 onto. Must be a player skull.
     * @param base64 The base64 string containing the texture.
     * @return The head with a custom texture.
     */
    fun itemWithBase64(item: ItemStack, base64: String): ItemStack? {
        notNull(item, "item")
        notNull(base64, "base64")
        if (item.itemMeta !is SkullMeta) {
            return null
        }
        val meta = item.itemMeta as SkullMeta
        mutateItemMeta(meta, base64)
        item.itemMeta = meta
        return item
    }

    /**
     * Sets the block to a skull with the given name.
     *
     * @param block The block to set.
     * @param name  The player to set it to.
     */
    @Deprecated("names don't make for good identifiers.")
    fun blockWithName(block: Block, name: String?) {
        notNull(block, "block")
        notNull(name, "name")
        val state = block.state as Skull
        state.owner = name
        state.update(false, false)
    }

    /**
     * Sets the block to a skull with the given UUID.
     *
     * @param block The block to set.
     * @param id    The player to set it to.
     */
    fun blockWithUuid(block: Block, id: UUID?) {
        notNull(block, "block")
        notNull(id, "id")
        setToSkull(block)
        val state = block.state as Skull
        state.owner = Bukkit.getOfflinePlayer(id!!).name
        state.update(false, false)
    }

    /**
     * Sets the block to a skull with the skin found at the provided mojang URL.
     *
     * @param block The block to set.
     * @param url   The mojang URL to set it to use.
     */
    fun blockWithUrl(block: Block, url: String?) {
        notNull(block, "block")
        notNull(url, "url")
        blockWithBase64(block, urlToBase64(url))
    }

    /**
     * Sets the block to a skull with the skin for the base64 string.
     *
     * @param block  The block to set.
     * @param base64 The base64 to set it to use.
     */
    fun blockWithBase64(block: Block, base64: String) {
        notNull(block, "block")
        notNull(base64, "base64")
        setToSkull(block)
        val state = block.state as Skull
        mutateBlockState(state, base64)
        state.update(false, false)
    }

    private fun setToSkull(block: Block) {
        checkLegacy()
        try {
            block.setType(Material.valueOf("PLAYER_HEAD"), false)
        } catch (e: IllegalArgumentException) {
            block.setType(Material.valueOf("SKULL"), false)
            val state = block.state as Skull
            state.skullType = SkullType.PLAYER
            state.update(false, false)
        }
    }

    private fun notNull(o: Any?, name: String) {
        if (o == null) {
            throw NullPointerException("$name should not be null!")
        }
    }

    private fun urlToBase64(url: String?): String {
        val actualUrl: URI
        actualUrl = try {
            URI(url)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
        val toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"$actualUrl\"}}}"
        return Base64.getEncoder().encodeToString(toEncode.toByteArray())
    }

    private fun makeProfile(b64: String): GameProfile {
        // random uuid based on the b64 string
        val id = UUID(
            b64.substring(b64.length - 20).hashCode().toLong(),
            b64.substring(b64.length - 10).hashCode().toLong()
        )
        val profile = GameProfile(id, "Player")
        profile.properties.put("textures", Property("textures", b64))
        return profile
    }

    private fun mutateBlockState(block: Skull, b64: String) {
        try {
            if (blockProfileField == null) {
                blockProfileField = block.javaClass.getDeclaredField("profile")
                metaSetProfileMethod!!.isAccessible = true
            }
            blockProfileField!![block] = makeProfile(b64)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    private fun mutateItemMeta(meta: SkullMeta, b64: String) {
        try {
            if (metaSetProfileMethod == null) {
                metaSetProfileMethod = meta.javaClass.getDeclaredMethod("setProfile", GameProfile::class.java)
                metaSetProfileMethod!!.isAccessible = true
            }
            metaSetProfileMethod!!.invoke(meta, makeProfile(b64))
        } catch (ex: NoSuchMethodException) {
            // if in an older API where there is no setProfile method,
            // we set the profile field directly.
            try {
                if (metaProfileField == null) {
                    metaProfileField = meta.javaClass.getDeclaredField("profile")
                    metaSetProfileMethod!!.isAccessible = true
                }
                metaProfileField!![meta] = makeProfile(b64)
            } catch (ex2: NoSuchFieldException) {
                ex2.printStackTrace()
            } catch (ex2: IllegalAccessException) {
                ex2.printStackTrace()
            }
        } catch (ex: IllegalAccessException) {
            try {
                if (metaProfileField == null) {
                    metaProfileField = meta.javaClass.getDeclaredField("profile")
                    metaSetProfileMethod!!.isAccessible = true
                }
                metaProfileField!![meta] = makeProfile(b64)
            } catch (ex2: NoSuchFieldException) {
                ex2.printStackTrace()
            } catch (ex2: IllegalAccessException) {
                ex2.printStackTrace()
            }
        } catch (ex: InvocationTargetException) {
            try {
                if (metaProfileField == null) {
                    metaProfileField = meta.javaClass.getDeclaredField("profile")
                    metaSetProfileMethod!!.isAccessible = true
                }
                metaProfileField!![meta] = makeProfile(b64)
            } catch (ex2: NoSuchFieldException) {
                ex2.printStackTrace()
            } catch (ex2: IllegalAccessException) {
                ex2.printStackTrace()
            }
        }
    }

    // suppress warning since PLAYER_HEAD doesn't exist in 1.12.2,
    // but we expect this and catch the error at runtime.
    private fun checkLegacy() {
        try {
            // if both of these succeed, then we are running
            // in a legacy api, but on a modern (1.13+) server.
            Material::class.java.getDeclaredField("PLAYER_HEAD")
            Material.valueOf("SKULL")
            if (!warningPosted) {
                Bukkit.getLogger()
                    .warning("SKULLCREATOR API - Using the legacy bukkit API with 1.13+ bukkit versions is not supported!")
                warningPosted = true
            }
        } catch (ignored: NoSuchFieldException) {
        } catch (ignored: IllegalArgumentException) {
        }
    }
}
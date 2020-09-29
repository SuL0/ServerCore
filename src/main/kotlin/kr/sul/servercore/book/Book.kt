package kr.sul.servercore.book

import com.comphenix.protocol.ProtocolLibrary
import io.netty.buffer.Unpooled
import net.md_5.bungee.api.chat.*
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.server.v1_12_R1.IChatBaseComponent
import net.minecraft.server.v1_12_R1.PacketDataSerializer
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import java.util.logging.Level
import java.util.logging.Logger

class Book {
    private val pm = ProtocolLibrary.getProtocolManager()

    companion object {
        val exampleBook: ItemStack
            get() {
                try {
                    val book = ItemStack(Material.WRITTEN_BOOK)
                    val meta = book.itemMeta as BookMeta
                    val pages = CraftMetaBook::class.java.getDeclaredField("pages")[meta] as MutableList<IChatBaseComponent?>
                    val text = TextComponent("§l§cExample\n")
                    val msg = TextComponent("§l§bClick me to go to spigotmc.org!\n")
                    msg.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "http://www.spigotmc.org")
                    msg.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("HI").create())
                    val msg2 = TextComponent("§l§aClick me to go to spawn")
                    msg2.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawn")
                    val page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(*arrayOf<BaseComponent>(text, msg, msg2)))
                    pages.add(page)
                    meta.title = "Example"
                    meta.author = "Optics Server"
                    book.itemMeta = meta
                    return book
                } catch (ex: NoSuchFieldException) {
                    Logger.getLogger(Book::class.java.name).log(Level.SEVERE, null, ex)
                } catch (ex: SecurityException) {
                    Logger.getLogger(Book::class.java.name).log(Level.SEVERE, null, ex)
                } catch (ex: IllegalArgumentException) {
                    Logger.getLogger(Book::class.java.name).log(Level.SEVERE, null, ex)
                } catch (ex: IllegalAccessException) {
                    Logger.getLogger(Book::class.java.name).log(Level.SEVERE, null, ex)
                }
                throw Exception()
            }
    }

    fun openBook(p: Player, book: ItemStack?) {       // 플레이어에게 책을 주는 과정이 무조건 있어야함
        val slot = p.inventory.heldItemSlot
        val old = p.inventory.getItem(slot)
        p.inventory.setItem(slot, book)

        // 1.15
        // p.openBook(player.getInventory().getItem(slot));

        // 1.12
        val buf = Unpooled.buffer(256)
        buf.setByte(0, 0)
        buf.writerIndex(1)
        val packet = PacketPlayOutCustomPayload("MC|BOpen", PacketDataSerializer(buf))
        (p as CraftPlayer).handle.playerConnection.sendPacket(packet)
        p.getInventory().setItem(slot, old)
    }
}
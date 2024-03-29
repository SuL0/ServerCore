package kr.sul.servercore.something

import com.comphenix.protocol.ProtocolLibrary
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.network.chat.Component
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftMetaBook
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta


// https://github.com/upperlevel/book-api  이게 더 나으려나
class Book {
    private val pm = ProtocolLibrary.getProtocolManager()

    companion object {
        val exampleBook: ItemStack
            get() {
                val book = ItemStack(Material.WRITTEN_BOOK)
                val meta = book.itemMeta as BookMeta
                val pages =
                    CraftMetaBook::class.java.getDeclaredField("pages")[meta] as MutableList<Component?>
                // 여기까지 신경 X

                val text = TextComponent("§l§cExample\n")

                val msg = TextComponent("§l§bClick me to go to spigotmc.org!\n").run {
                    clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "http://www.spigotmc.org")
                    hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("HI").create())
                    this
                }
                val msg2 = TextComponent("§l§aClick me to go to spawn").run {
                    clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawn")
                    this
                }

                val page = Component.Serializer.fromJson(ComponentSerializer.toString(text, msg, msg2))
                pages.add(page)  // 1페이지에 page(text, msg, msg2 라는 문자를 포함한 페이지) 를 넣음


                // 책 제목, 작자, 기타
                meta.title = "Example"
                meta.author = "Optics Server"
                book.itemMeta = meta
                return book
            }
    }

    fun openBook(p: Player, book: ItemStack?) {       // 플레이어에게 책을 주는 과정이 무조건 있어야함
        val slot = p.inventory.heldItemSlot
        val old = p.inventory.getItem(slot)
        p.inventory.setItem(slot, book)

        // 1.15
         p.openBook(p.inventory.getItem(slot)!!)
    }

//    fun d() {
//        val page1 = PageBuilder().add("     < 가이드 북 >\n")
//            .add(BookUtil.TextBuilder.of("[여기]").onHover(BookUtil.HoverAction.showText(
//                    "[ 노말에서 살아남기 ]\n" +
//                            "기록의 문을 넘어 노말 난이도에 입장하세요.\n" +
//                            "입장하신 뒤에 노말 난이도가 쉬워질 때 까지\n" +
//                            "적응 하셔야 합니다.\n")).build()
//            )
//            .add(BookUtil.TextBuilder.of(" 노말에서 살아남기\n").build())
//            .add(BookUtil.TextBuilder.of(" [스폰으로 가기]").onClick(BookUtil.ClickAction.runCommand("/spawn")).build())
//            .build()
//
//
//        BookUtil.writtenBook()
//            .author("저자").title("제목")
//            .pages(page1)
//            .build()
//    }
}
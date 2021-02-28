package kr.sul.servercore.getconfirmgui

import kr.sul.servercore.util.ItemBuilder.loreIB
import kr.sul.servercore.util.ItemBuilder.nameIB
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class ConfirmGui(plugin: Plugin,
                 private val p: Player, private val title: String,
                 private val askItem: ItemStack, private val confirmItem: ItemStack, private val denyItem: ItemStack,
                 private val onConfirm: Runnable, private val onDeny: Runnable?, private val onClose: Runnable?) {
    private lateinit var inventory: Inventory
    private val listener = ListenUp()
    init {
        Bukkit.getPluginManager().registerEvents(listener, plugin)
    }

    fun open() {
        inventory = Bukkit.createInventory(null, 27, title)
        inventory.setItem(4, askItem)
        inventory.setItem(20, confirmItem)
        inventory.setItem(24, denyItem)
        p.openInventory(inventory)
    }


    inner class ListenUp: Listener {
        @EventHandler
        fun onInvClick(e: InventoryClickEvent) {
            if (e.clickedInventory == inventory) {
                e.isCancelled = true
                if (e.currentItem == confirmItem) {
                    onConfirm.run()
                    e.whoClicked.closeInventory()
                } else if (e.currentItem == denyItem) {
                    onDeny?.run()
                    e.whoClicked.closeInventory()
                }
            }
        }

        @EventHandler
        fun onInvClose(e: InventoryCloseEvent) {
            if (e.inventory == inventory) {
                onClose?.run()
                HandlerList.unregisterAll(this)
            }
        }
    }


    /* 현재는 중첩 클래스 (내부 참조 X, Java: static class)
       앞에 inner 을 붙이면 내부 클래스 (내부 참조 O, Java: non-static class, 외부 객체를 통해 생성 가능, 외부 참조가 유지됨에 따라 메모리 누수 위험)
       // inner class 를 밑의 상황 빼고는 상속이랑 비슷하다고 볼 수 있나?

       val in = Outer().Inner()
       in.outerMember = 7 (X)
       // 단 Inner 내부 메소드에선 outerMember에 접근 가능
    */
    class Builder {
        private lateinit var plugin: Plugin
        private lateinit var title: String
        private lateinit var askItem: ItemStack
        private var confirmItem: ItemStack = ItemStack(Material.GREEN_SHULKER_BOX).nameIB("§2§l확인").loreIB("§7위의 사항에 대해 동의합니다.", 2)
        private var denyItem: ItemStack = ItemStack(Material.RED_SHULKER_BOX).nameIB("§c§l거절").loreIB("§7위의 사항에 대해 거절합니다.", 2)
        private lateinit var onConfirm: Runnable
        private var onDeny: Runnable? = null
        private var onClose: Runnable? = null

        fun plugin(plugin: Plugin): Builder {
            this.plugin = plugin
            return this
        }
        fun title(str: String): Builder {
            this.title = str
            return this
        }
        fun askItem(item: ItemStack): Builder {
            this.askItem = item
            return this
        }
        fun confirmItem(item: ItemStack): Builder {
            this.confirmItem = item
            return this
        }
        fun denyItem(item: ItemStack): Builder {
            this.denyItem = item
            return this
        }
        fun onConfirm(runnable: Runnable): Builder {
            this.onConfirm = runnable
            return this
        }
        fun onDeny(runnable: Runnable): Builder {
            this.onDeny = runnable
            return this
        }
        fun onClose(runnable: Runnable): Builder {
            this.onClose = runnable
            return this
        }

        // build().open()을 간략화시킴
        fun open(p: Player): ConfirmGui {
            val confirmGui = ConfirmGui(plugin, p, title, askItem, confirmItem, denyItem, onConfirm, onDeny, onClose)
            confirmGui.open()
            return confirmGui
        }
    }
}

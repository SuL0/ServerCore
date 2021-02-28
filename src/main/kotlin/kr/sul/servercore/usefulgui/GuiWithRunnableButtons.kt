package kr.sul.servercore.usefulgui

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class GuiWithRunnableButtons(plugin: Plugin,
                             private val p: Player,
                             private val title: String, private val lines: Int,
                             private val runnableButtonList: List<RunnableButton>,
                             private val onClose: Runnable?) {
    private lateinit var inventory: Inventory
    private val listener = ListenUp()
    init {
        Bukkit.getPluginManager().registerEvents(listener, plugin)
    }


    fun open() {
        inventory = Bukkit.createInventory(null, lines*9, title)
        runnableButtonList.forEach {
            inventory.addItem(it.item)
        }
        p.openInventory(inventory)
    }

    inner class ListenUp: Listener {
        @EventHandler
        fun onClick(e: InventoryClickEvent) {
            if (e.clickedInventory == inventory) {
                e.isCancelled = true
                // index 기반으로 인식
                if (e.slot <= runnableButtonList.size) {
                    runnableButtonList[e.slot].onClick.run()
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




    data class RunnableButton(val item: ItemStack, val onClick: Runnable)

    class Builder {
        private lateinit var plugin: Plugin
        private lateinit var title: String
        private var lines = -1
        private val runnableButtonList = arrayListOf<RunnableButton>()
        private var onClose: Runnable? = null


        fun plugin(plugin: Plugin): Builder {
            this.plugin = plugin
            return this
        }
        fun title(title: String): Builder {
            this.title = title
            return this
        }
        fun lines(i: Int): Builder {
            this.lines = i
            return this
        }
        fun addRunnableButton(item: ItemStack, onClick: Runnable): Builder {
            val runnableButton = RunnableButton(item, onClick)
            runnableButtonList.add(runnableButton)
            return this
        }
        fun onClose(runnable: Runnable): Builder {
            onClose = runnable
            return this
        }

        // build().open()을 간략화시킴
        fun open(p: Player): GuiWithRunnableButtons {
            val guiWithRunnable = GuiWithRunnableButtons(plugin, p, title, lines, runnableButtonList, onClose)
            guiWithRunnable.open()
            return guiWithRunnable
        }
    }
}
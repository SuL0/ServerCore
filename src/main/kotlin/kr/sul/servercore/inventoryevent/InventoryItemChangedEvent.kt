package kr.sul.servercore.inventoryevent

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

// NOTE: 인벤토리 36칸 안에 있는 어떠한 아이템이든 변화가 있을 때 호출되는 이벤트
class InventoryItemChangedEvent(val player: Player, val slot: Int, val newItemStack: ItemStack) : Event() {

    override fun getHandlers(): HandlerList { return handlerList }
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
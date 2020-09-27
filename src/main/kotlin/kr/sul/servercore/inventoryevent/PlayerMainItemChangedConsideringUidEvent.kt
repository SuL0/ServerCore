package kr.sul.servercore.inventoryevent

import kr.sul.crackshotaddition.util.CrackShotAdditionAPI
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

// NOTE: 핫바를 통해서던 인벤토리에서 아이템을 바꾸던, 손에 들고 있는 아이템이 바뀌었을 때 호출되는 이벤트 (아이템에 UID가 있을 시 UID의 변화가 있었는지도 확인)
class PlayerMainItemChangedConsideringUidEvent
        (val player: Player, val clonedPreviousItemStack: ItemStack, val newItemStack: ItemStack, val newSlot: Int) : Event() {
    // clonedPreviousItemStack은 정확하지 않을 수 있음 !

    fun isChangedToCrackShotWeapon(): Boolean {
        return CrackShotAdditionAPI.isValidCrackShotWeapon(newItemStack)
    }

    override fun getHandlers(): HandlerList { return handlerList }
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
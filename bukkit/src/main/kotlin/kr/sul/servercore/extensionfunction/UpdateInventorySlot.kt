package kr.sul.servercore.extensionfunction

import kr.sul.servercore.util.InventorySlotConverterUtil
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object UpdateInventorySlot {
    enum class HandType {
        MAIN_HAND,
        OFF_HAND
    }

    // INFO: updateInventory는 ItemMeta변경을 즉각 확인하고 싶을 때(이름, 설명) 외에는 어차피 바로 반영되니 쓸 필요가 없음.
    fun Player.updateInventorySlot(handType: HandType) {
        val spigotSlot: Int = when(handType) {
            HandType.MAIN_HAND -> this.inventory.heldItemSlot
            HandType.OFF_HAND -> 40
        }
        updateInventorySlot(spigotSlot)
    }
    fun Player.updateInventorySlot(spigotSlot: Int) {
        val handle = (this as CraftPlayer).handle
        val nmsSlot = InventorySlotConverterUtil.spigotSlotToNmsSlot(spigotSlot)
        val item: ItemStack = this.inventory.getItem(spigotSlot) ?: return

        val nmsItem: net.minecraft.world.item.ItemStack = CraftItemStack.asNMSCopy(item)
        // To set the cursor (the item currently dragged with the mouse), use -1 as Window ID and as Slot.
        // This packet can only be used to edit the hotbar of the player's inventory if window ID is set to 0 (slots 36 through 44). If the window ID is set to -2, then any slot in the inventory can be used but no add item animation will be played.
        // 근데 다들 그냥 다 0으로 씀
        val packet = ClientboundContainerSetSlotPacket(0, this.handle.containerMenu.incrementStateId(), nmsSlot, nmsItem)
        handle.connection.send(packet)
    }
}
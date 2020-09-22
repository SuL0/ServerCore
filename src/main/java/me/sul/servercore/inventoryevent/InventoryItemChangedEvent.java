package me.sul.servercore.inventoryevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

// NOTE: 인벤토리 36칸 안에 있는 어떠한 아이템이든 변화가 있을 때 호출되는 이벤트
public class InventoryItemChangedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final int slot;
    private final ItemStack is;


    public InventoryItemChangedEvent(Player player, int slot, ItemStack is) {
        this.player = player;
        this.slot = slot;
        this.is = is;
    }

    public Player getPlayer() { return player; }
    public int getSlot() { return slot; }
    public ItemStack getItemStack() { return is; }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() { return handlers; }
}

package me.sul.servercore.playertoolchangeevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class InventoryItemChangedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private int slot;
    private ItemStack is;


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

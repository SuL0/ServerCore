package me.sul.servercore.playertoolchangeevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerMainItemChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private ItemStack previousItem;         // ! 정확하지 않을 수 있음 !
    private ItemStack newItem;


    public PlayerMainItemChangeEvent(Player player, ItemStack previousItem, ItemStack newItem) {
        this.player = player;
        this.previousItem = previousItem;
        this.newItem = newItem;
    }

    public Player getPlayer() { return player; }
    public ItemStack getPreviousItem() { return previousItem; }
    public ItemStack getNewItem() { return newItem; }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() { return handlers; }
}

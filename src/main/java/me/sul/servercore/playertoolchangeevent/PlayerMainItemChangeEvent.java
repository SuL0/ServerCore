package me.sul.servercore.playertoolchangeevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerMainItemChangeEvent extends Event { // 아이템을 바꾸지 않고, 핫바를 바꿔서 MainHand의 아이템이 바뀔 수도 있기 때문에 Inventory보다는 Player가 더 적합함.
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private ItemStack clonedPreviousItem;         // ! 정확하지 않을 수 있음 !
    private ItemStack newItem;


    public PlayerMainItemChangeEvent(Player player, ItemStack clonedPreviousItem, ItemStack newItem) {
        this.player = player;
        this.clonedPreviousItem = clonedPreviousItem;
        this.newItem = newItem;
    }

    public Player getPlayer() { return player; }
    public ItemStack getClonedPreviousItemClone() { return clonedPreviousItem; }
    public ItemStack getNewItem() { return newItem; }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() { return handlers; }
}

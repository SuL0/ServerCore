package me.sul.servercore.playertoolchangeevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerMainItemChangedEvent extends Event { // 아이템을 바꾸지 않고, 핫바를 바꿔서 MainHand의 아이템이 바뀔 수도 있기 때문에 Inventory보다는 Player가 더 적합함.
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private ItemStack clonedPreviousItemStack;         // ! 정확하지 않을 수 있음 !
    private ItemStack newItemStack;


    public PlayerMainItemChangedEvent(Player player, ItemStack clonedPreviousItemStack, ItemStack newItemStack) {
        this.player = player;
        this.clonedPreviousItemStack = clonedPreviousItemStack;
        this.newItemStack = newItemStack;
    }

    public Player getPlayer() { return player; }
    public ItemStack getClonedPreviousItemStack() { return clonedPreviousItemStack; }
    public ItemStack getNewItemStack() { return newItemStack; }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() { return handlers; }
}

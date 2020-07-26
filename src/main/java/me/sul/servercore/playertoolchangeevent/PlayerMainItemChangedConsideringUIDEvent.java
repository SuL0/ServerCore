package me.sul.servercore.playertoolchangeevent;

import me.sul.crackshotaddition.util.CrackShotAdditionAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

// NOTE: 핫바를 통해서던 인벤토리에서 아이템을 바꾸던, 손에 들고 있는 아이템이 바뀌었을 때 호출되는 이벤트 (아이템에 UID가 있을 시 UID의 변화가 있었는지도 확인)
public class PlayerMainItemChangedConsideringUIDEvent extends Event { // 아이템을 바꾸지 않고, 핫바를 바꿔서 MainHand의 아이템이 바뀔 수도 있기 때문에 Inventory보다는 Player가 더 적합함.
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private ItemStack clonedPreviousItemStack;         // ! 정확하지 않을 수 있음 !
    private ItemStack newItemStack;
    private boolean isChangedToCrackShotWeapon;


    public PlayerMainItemChangedConsideringUIDEvent(Player player, ItemStack clonedPreviousItemStack, ItemStack newItemStack) {
        this.player = player;
        this.clonedPreviousItemStack = clonedPreviousItemStack;
        this.newItemStack = newItemStack;
        isChangedToCrackShotWeapon = CrackShotAdditionAPI.isValidCrackShotWeapon(newItemStack);
    }

    public Player getPlayer() { return player; }
    public ItemStack getClonedPreviousItemStack() { return clonedPreviousItemStack; }
    public ItemStack getNewItemStack() { return newItemStack; }
    public boolean isChangedToCrackShotWeapon() { return isChangedToCrackShotWeapon; }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() { return handlers; }
}

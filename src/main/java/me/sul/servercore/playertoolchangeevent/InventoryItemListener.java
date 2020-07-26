package me.sul.servercore.playertoolchangeevent;

import me.sul.servercore.serialnumber.UniqueIdAPI;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryItemListener implements Listener {
    HashMap<Player, ItemStack> mainItemOfPlayers = new HashMap<>();  // defaultContainer.addSlotListener() - a()에서 previousIs 얻을 방법이 없기때문.


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        mainItemOfPlayers.put(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand());

        EntityPlayer ep = ((CraftPlayer)e.getPlayer()).getHandle();
        ep.defaultContainer.addSlotListener(new ICrafting() {
            final Player p = e.getPlayer();

            @Override
            public void a(Container container, NonNullList<net.minecraft.server.v1_12_R1.ItemStack> nonNullList) {}

            @Override
            public void a(Container container, int nmsSlot, net.minecraft.server.v1_12_R1.ItemStack nmsIs) {
                int newSlot = InventorySlotConverterUtil.nmsSlotToSpigotSlot(nmsSlot);
                ItemStack newIs = p.getInventory().getItem(newSlot) != null ? p.getInventory().getItem(newSlot) : new ItemStack(Material.AIR);
//                ItemStack newIs = CraftItemStack.asBukkitCopy(nmsIs); // 이렇게 하면 clone된거라서 아이템의 ItemMeta를 수정하기가 번거로워짐.

                Bukkit.getPluginManager().callEvent(new InventoryItemChangedEvent(p, newSlot, newIs)); // EVENT: Call InventoryItemChangedEvent

                // "슬롯을 바꾸지 않은 채로" 손에 든 아이템이 바꼈을 때. 아이템 정보 바뀌는건 제외(시리얼번호 비교. 없다면 그냥 계속 중복되게 호출됨)
                if (newSlot == p.getInventory().getHeldItemSlot()) {
                    ItemStack clonedPreviousMainIs = mainItemOfPlayers.containsKey(p) ? mainItemOfPlayers.get(p) : new ItemStack(Material.AIR);
                    if (UniqueIdAPI.hasUniqueID(clonedPreviousMainIs) && UniqueIdAPI.hasUniqueID(newIs) &&
                            UniqueIdAPI.getUniqueID(clonedPreviousMainIs).equals(UniqueIdAPI.getUniqueID(newIs)))
                        return;

                    Bukkit.getPluginManager().callEvent(new PlayerMainItemChangedEvent(p, clonedPreviousMainIs, newIs)); // EVENT: Call PlayerMainItemChangeEvent
                    mainItemOfPlayers.put(p, newIs);
                }
            }

            @Override
            public void setContainerData(Container container, int i, int i1) {}

            @Override
            public void setContainerData(Container container, IInventory iInventory) {}
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemHeld(PlayerItemHeldEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        ItemStack clonedPreviousIs = p.getInventory().getItem(e.getPreviousSlot()) != null ? e.getPlayer().getInventory().getItem(e.getPreviousSlot()).clone() : new ItemStack(Material.AIR); // clone()은 일부러 a()에서 PreviousItem ItemMeta 수정 못하는걸 동일시 하기 위해서 넣었음.
        ItemStack newIs = p.getInventory().getItem(e.getNewSlot()) != null ? e.getPlayer().getInventory().getItem(e.getNewSlot()) : new ItemStack(Material.AIR);
        if (clonedPreviousIs.getType().equals(Material.AIR) && newIs.getType().equals(Material.AIR)) return;

        Bukkit.getPluginManager().callEvent(new PlayerMainItemChangedEvent(p, clonedPreviousIs, newIs));
        mainItemOfPlayers.put(p, newIs);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        mainItemOfPlayers.remove(e.getPlayer());
    }
}

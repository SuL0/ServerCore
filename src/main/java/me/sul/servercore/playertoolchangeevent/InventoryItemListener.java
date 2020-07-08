package me.sul.servercore.playertoolchangeevent;

import me.sul.crackshotaddition.util.CrackShotAdditionAPI;
import me.sul.servercore.serialnumber.SerialNumberAPI;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
            Player player = e.getPlayer();

            @Override
            public void a(Container container, NonNullList<net.minecraft.server.v1_12_R1.ItemStack> nonNullList) {}

            @Override
            public void a(Container container, int nmsSlot, net.minecraft.server.v1_12_R1.ItemStack nmsIs) {
                int newSlot = InventorySlotConverterUtil.nmsSlotToSpigotSlot(nmsSlot);
                ItemStack previousIs = mainItemOfPlayers.containsKey(player) ? mainItemOfPlayers.get(player) : new ItemStack(Material.AIR);
                ItemStack newIs = CraftItemStack.asBukkitCopy(nmsIs);

                Bukkit.getPluginManager().callEvent(new InventoryItemChangedEvent(player, newSlot, newIs)); // Call InventoryItemChangedEvent

                // "슬롯을 바꾸지 않은 채로" 손에 든 아이템이 바꼈을 때
                if (newSlot == player.getInventory().getHeldItemSlot()) {
                    if (SerialNumberAPI.hasSerialNumber(previousIs) && SerialNumberAPI.hasSerialNumber(newIs) &&
                            SerialNumberAPI.getSerialNumber(previousIs) == SerialNumberAPI.getSerialNumber(newIs)) return; // 아이템 정보 바뀌는건 제외(시리얼번호가 이전과 같은지 여부로 확인)

                    // 이거 AIR도 put되긴하나?
                    Bukkit.getPluginManager().callEvent(new PlayerMainItemChangeEvent(player, previousIs, newIs)); // Call PlayerMainItemChangeEvent
                    Bukkit.getServer().broadcastMessage("[ServerCore] MainItemOfPlayer ItemStack: " + newIs.getType().toString());
                    mainItemOfPlayers.put(player, newIs);
                }
            }

            @Override
            public void setContainerData(Container container, int i, int i1) {}

            @Override
            public void setContainerData(Container container, IInventory iInventory) {}
        });
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack previousIs = p.getInventory().getItem(e.getPreviousSlot()) != null ? e.getPlayer().getInventory().getItem(e.getPreviousSlot()) : new ItemStack(Material.AIR);
        ItemStack newIs = p.getInventory().getItem(e.getNewSlot()) != null ? e.getPlayer().getInventory().getItem(e.getNewSlot()) : new ItemStack(Material.AIR);
        if (previousIs.getType().equals(Material.AIR) && newIs.getType().equals(Material.AIR)) return;

        Bukkit.getPluginManager().callEvent(new PlayerMainItemChangeEvent(p, previousIs, newIs));
        mainItemOfPlayers.put(p, newIs);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        mainItemOfPlayers.remove(e.getPlayer());
    }
}

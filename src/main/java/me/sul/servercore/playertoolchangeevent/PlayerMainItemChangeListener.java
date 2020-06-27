package me.sul.servercore.playertoolchangeevent;

import me.sul.crackshotaddition.util.CrackShotAPI;
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

public class PlayerMainItemChangeListener implements Listener {
    HashMap<Player, ItemStack> mainItemOfPlayers = new HashMap<Player, ItemStack>();


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        mainItemOfPlayers.put(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand());

        EntityPlayer ep = ((CraftPlayer)e.getPlayer()).getHandle();
        ep.defaultContainer.addSlotListener(new ICrafting() {
            Player p = e.getPlayer();

            @Override
            public void a(Container container, NonNullList<net.minecraft.server.v1_12_R1.ItemStack> nonNullList) {}

            @Override
            public void a(Container container, int i, net.minecraft.server.v1_12_R1.ItemStack itemStack) {
                Bukkit.getServer().broadcastMessage("inventory Listener");
                int eventSlot = InventorySlotConverterUtil.nmsSlotToSpigotSlot(i);
                ItemStack previousIs = mainItemOfPlayers.containsKey(p) ? mainItemOfPlayers.get(p) : new ItemStack(Material.AIR);
                ItemStack newIs = CraftItemStack.asBukkitCopy(itemStack);
                if (eventSlot == p.getInventory().getHeldItemSlot()) {
                    String previousWeaponTitle = CrackShotAPI.getWeaponTitle(previousIs);
                    String newWeaponTitle = CrackShotAPI.getWeaponTitle(newIs);
                    if (previousWeaponTitle != null && newWeaponTitle != null) {
                        if (previousWeaponTitle.equals(newWeaponTitle)) {  // ÃÑ±â ÃÑ¾Ë ÀÌ¸§ ¹Ù²î´Â°Ç Á¦¿Ü -> previousIs°¡ Á¤È®ÇÏÁö ¾Ê°ÔµÊ
                            return;
                        }
                    }
                    Bukkit.getPluginManager().callEvent(new PlayerMainItemChangeEvent(p, previousIs, newIs));
                    mainItemOfPlayers.put(p, newIs);
                }
            }

            @Override
            public void setContainerData(Container container, IInventory iInventory) { }
        });
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack previousIs = p.getInventory().getItem(e.getPreviousSlot()) != null ? e.getPlayer().getInventory().getItem(e.getPreviousSlot()) : new ItemStack(Material.AIR);
        ItemStack newIs = p.getInventory().getItem(e.getNewSlot()) != null ? e.getPlayer().getInventory().getItem(e.getNewSlot()) : new ItemStack(Material.AIR);
        if (previousIs.getType().equals(Material.AIR) && newIs.getType().equals(Material.AIR)) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new PlayerMainItemChangeEvent(p, previousIs, newIs));
        mainItemOfPlayers.put(p, newIs);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        mainItemOfPlayers.remove(e.getPlayer());
    }
}

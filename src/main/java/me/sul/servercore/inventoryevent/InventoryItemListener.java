package me.sul.servercore.inventoryevent;

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
    HashMap<Player, ItemStack> clonedMainItemOfPlayers = new HashMap<>();


    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        EntityPlayer ep = ((CraftPlayer)e.getPlayer()).getHandle();
        ep.defaultContainer.addSlotListener(new ICrafting() {
            final Player p = e.getPlayer();

            @Override
            public void a(Container container, NonNullList<net.minecraft.server.v1_12_R1.ItemStack> nonNullList) {}

            @Override
            public void a(Container container, int nmsSlot, net.minecraft.server.v1_12_R1.ItemStack nmsIs) {
                int newSlot = InventorySlotConverterUtil.nmsSlotToSpigotSlot(nmsSlot);
                ItemStack newIs = p.getInventory().getItem(newSlot) != null ? p.getInventory().getItem(newSlot) : new ItemStack(Material.AIR);

                // "슬롯을 바꾸지 않은 채로" 손에 든 아이템이 바꼈을 때. 아이템 정보 바뀌는건 제외(UID 비교. 없다면 그냥 계속 중복되게 호출됨)
                if (newSlot == p.getInventory().getHeldItemSlot()) {
                    ItemStack clonedPreviousMainIs = clonedMainItemOfPlayers.containsKey(p) ? clonedMainItemOfPlayers.get(p) : new ItemStack(Material.AIR);
                    if (!(UniqueIdAPI.hasUniqueID(clonedPreviousMainIs) && UniqueIdAPI.hasUniqueID(newIs) &&
                            UniqueIdAPI.getUniqueID(clonedPreviousMainIs).equals(UniqueIdAPI.getUniqueID(newIs)))) {
                        Bukkit.getPluginManager().callEvent(new PlayerMainItemChangedConsideringUidEvent(p, clonedPreviousMainIs, newIs, newSlot)); // EVENT: Call PlayerMainItemChangeEvent
                        clonedMainItemOfPlayers.put(p, newIs.clone());
                    }
                }
                Bukkit.getPluginManager().callEvent(new InventoryItemChangedEvent(p, newSlot, newIs)); // EVENT: Call InventoryItemChangedEvent
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

        Bukkit.getPluginManager().callEvent(new PlayerMainItemChangedConsideringUidEvent(p, clonedPreviousIs, newIs, e.getNewSlot()));
        clonedMainItemOfPlayers.put(p, newIs.clone()); // NOTE: 씨발 Drop이벤트가 취소되든 말든간에 ItemStack이 AIR로 바뀌는 버그때문에 .clone해서 저장해야 함.
                                                       // NOTE: 근데 어차피 상관없는게, 정상적으로 아이템이 바꼈으면 이벤트에 걸려서 수정되게 돼있음.
    }






//    // ServerCore - InventoryListener 테스트 코드
//    @EventHandler(priority = EventPriority.LOWEST)
//    public void onInventoryOpen(InventoryOpenEvent e) {
//        e.getInventory().setItem(0, new ItemStack(Material.GOLD_AXE));
//        EntityPlayer ep = ((CraftPlayer)e.getPlayer()).getHandle();
//        // 1틱 전의 변화는 감지를 못함
//        Bukkit.getScheduler().runTaskLater(CrackShotAddition.getInstance(), () -> {
//            Bukkit.getServer().broadcastMessage("getTopInventoryType: " + ep.activeContainer.getBukkitView().getTopInventory().getType());
//            Bukkit.getServer().broadcastMessage("getBottomInventoryType: " + ep.activeContainer.getBukkitView().getBottomInventory().getType());
//            ep.activeContainer.addSlotListener(new ICrafting() {
//                @Override
//                public void a(Container container, NonNullList<net.minecraft.server.v1_12_R1.ItemStack> nonNullList) {
//
//                }
//
//                @Override
//                public void a(Container container, int nmsSlot, net.minecraft.server.v1_12_R1.ItemStack itemStack) {
//                    Bukkit.getServer().broadcastMessage("§ca() - §f" + container.getBukkitView().getType());
//                    Bukkit.getServer().broadcastMessage("  slot: " + nmsSlot);
//                }
//
//                @Override
//                public void setContainerData(Container container, int i, int i1) {
//
//                }
//
//                @Override
//                public void setContainerData(Container container, IInventory iInventory) {
//
//                }
//            });
//        }, 1L);
//    }
//
//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent e) {
//        Bukkit.getServer().broadcastMessage("InventoryClickEvent: " + e.getSlot());
//    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        clonedMainItemOfPlayers.remove(e.getPlayer());
    }
}
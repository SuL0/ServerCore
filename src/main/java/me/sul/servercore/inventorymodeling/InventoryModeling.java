package me.sul.servercore.inventorymodeling;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.sul.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

// 원본 코드 출처 : https://github.com/AgentTroll/static-player-inv/blob/master/src/main/java/com/gmail/woodyc40/staticplayerinv/Main.java
public class InventoryModeling implements Listener {
    private static final int PLAYER_CRAFT_INV_SIZE = 5;
    private static final long INV_UPDATE_INTERVAL = 20L;
    private Map<Player, BukkitTask> itemRetentionTasks =
            new HashMap<>();
    private final ItemStack INVENTORY_MODELING_ITEM;
    private final ItemStack BUTTON1;
    private final ItemStack BUTTON2;
    private final ItemStack BUTTON3;
    private final ItemStack BUTTON4;

    private ProtocolManager pm = ProtocolLibrary.getProtocolManager();

    public InventoryModeling() {
        INVENTORY_MODELING_ITEM = new ItemStack(Material.GOLD_HOE, 1, (short)16);

        BUTTON1 = new ItemStack(Material.STONE);
        ItemStack button2 = new ItemStack(Material.STONE);
        ItemMeta button2Meta = button2.getItemMeta();
        button2Meta.setDisplayName("2번 버튼");
        button2.setItemMeta(button2Meta);
        BUTTON2 = button2;

        BUTTON3 = new ItemStack(Material.STONE);
        ItemStack button4 = new ItemStack(Material.STONE);
        ItemMeta button4Meta = button4.getItemMeta();
        button4Meta.setDisplayName("4번 버튼");
        button4.setItemMeta(button4Meta);
        BUTTON4 = button4;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(ServerCore.getInstance(), () -> {
            InventoryView view = player.getOpenInventory();
            if (isPlayerInv(view)) {  // 인벤 제외한 GUI열면 false
                sendInventoryModelingItemPacket(player);
            }
        }, 0L, INV_UPDATE_INTERVAL/5);
        this.itemRetentionTasks.put(player, task);

        Bukkit.getScheduler().runTaskLater(ServerCore.getInstance(), () -> {
            sendButtonItemPacket(player);
        }, 1);


    }

    private void sendInventoryModelingItemPacket(Player player) { // 인벤토리 닫겨있을 때 패킷을 아무리 넣어봐야 의미가 없어서, 주기를 짧게 줘야 함
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.SET_SLOT);
        packet.getIntegers().write(0, 0).write(1, 0);
        packet.getItemModifier().write(0, INVENTORY_MODELING_ITEM);
        try {
            pm.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private void sendButtonItemPacket(Player player) { // CraftingTable의 4칸슬롯에 packet을 보내면 '죽지 않는 한' 그대로 있음. 심지어 게임모드를 바꾸더라도.
        for (int slot=1; slot<=4; slot++) {
            PacketContainer packet = pm.createPacket(PacketType.Play.Server.SET_SLOT);
            packet.getIntegers().write(0, 0).write(1, slot);
            if (slot==1)
                packet.getItemModifier().write(0, BUTTON1);
            else if (slot==2)
                packet.getItemModifier().write(0, BUTTON2);
            else if (slot==3)
                packet.getItemModifier().write(0, BUTTON3);
            else if (slot==4)
                packet.getItemModifier().write(0, BUTTON4);
            try {
                pm.sendServerPacket(player, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BukkitTask task = this.itemRetentionTasks.remove(player);
        if (task != null) {
            task.cancel();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        Player player = (Player) event.getWhoClicked();
        if (isPlayerInv(view)) {
            if (event.getClickedInventory() != player.getInventory()) {
                InventoryAction invAction = event.getAction();
                if (invAction == InventoryAction.PLACE_ALL || invAction == InventoryAction.PLACE_SOME ||
                        invAction == InventoryAction.PLACE_ONE || invAction == InventoryAction.HOTBAR_SWAP) event.setCancelled(true);

                // NOTE: 크래프팅 테이블 5칸이 한 몸처럼 작동해서 하나 클릭하면 5개 다 템이 사라짐
                Bukkit.getScheduler().runTaskLater(ServerCore.getInstance(), () -> {
                    InventoryView view2 = player.getOpenInventory();
                    if (isPlayerInv(view2)) {
                        sendInventoryModelingItemPacket(player);
                        sendButtonItemPacket(player);
                    }
                }, 1L);
            } else {
                if (event.getSlot() >= 36 && event.getSlot() <= 39) {
                    Bukkit.getScheduler().runTaskLater(ServerCore.getInstance(), () -> {
                        InventoryView view2 = player.getOpenInventory();
                        if (isPlayerInv(view2)) {
                            sendInventoryModelingItemPacket(player);
                        }
                    }, 1L);
                } else if (event.getSlot() == 40) {
                    Bukkit.getScheduler().runTaskLater(ServerCore.getInstance(), () -> {
                        InventoryView view2 = player.getOpenInventory();
                        if (isPlayerInv(view2)) {
                            sendInventoryModelingItemPacket(player);
                            sendButtonItemPacket(player);
                        }
                    }, 1L);
                }
            }
        }
    }
    @EventHandler
    public void onDeath(PlayerRespawnEvent e) {
        sendButtonItemPacket(e.getPlayer());
    }

    private static boolean isPlayerInv(InventoryView view) {
        return view.getTopInventory().getSize() == PLAYER_CRAFT_INV_SIZE;
    }


//    // https://www.spigotmc.org/threads/detect-inventory-open-e.257205/ -> 1.12에선 Achievement가 사라졌다.
//    private void addInventoryOpenListener() {
//        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ServerCore.getInstance(), PacketType.Play.Client.CLIENT_COMMAND){
//            @Override
//            public void onPacketReceiving(PacketEvent event) {
//
//                if (event.getPacket().getClientCommands().read(0) == EnumWrappers.ClientCommand.OPEN_INVENTORY_ACHIEVEMENT){
//                    Player player = event.getPlayer();
//                    Bukkit.getServer().broadcastMessage("inventory open -> " + player);
//                }
//            }
//        });
//    }
}

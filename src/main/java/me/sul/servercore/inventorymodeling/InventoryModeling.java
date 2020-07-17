package me.sul.servercore.inventorymodeling;

import me.sul.servercore.ServerCore;
import me.sul.servercore.playertoolchangeevent.InventoryItemChangedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

// 원본 코드 출처 : https://github.com/AgentTroll/static-player-inv/blob/master/src/main/java/com/gmail/woodyc40/staticplayerinv/Main.java
// NOTE: 패킷으로 조합대에 템을 넣는 방법은 p.updateInventory()를 하면 아이템이 다 날아감. 근데 1초마다 자동 update되는건 안그럼.

// NOTE: 서버를 닫기 전 Kick을 하면 상관없는데, 유저를 추방하지 않고 닫아버리면 아이템이 드랍됨. -> 서버에서 나갈 때 onClose()가 호출됨.
// NOTE: onClose()에 의해서 아이템이 삭제되고, 1틱뒤에 아이템이 다시 설정되려고 하는데 유저는 이미 나갔으므로 무시됨.
public class InventoryModeling implements Listener {
    private static final int PLAYER_CRAFT_INV_SIZE = 5;
    private final ItemStack INVENTORY_MODELING_ITEM;
    private final ItemStack BUTTON1;
    private final ItemStack BUTTON2;
    private final ItemStack BUTTON3;
    private final ItemStack BUTTON4;

    public InventoryModeling() {
        // 아이템 추가
        BUTTON1 = new ItemStack(Material.GOLD_HOE);
        ItemStack button2 = new ItemStack(Material.GOLD_HOE);
        ItemMeta button2Meta = button2.getItemMeta();
        button2Meta.setDisplayName("2번 버튼");
        button2.setItemMeta(button2Meta);
        BUTTON2 = button2;

        BUTTON3 = new ItemStack(Material.GOLD_HOE);
        ItemStack button4 = new ItemStack(Material.GOLD_HOE);
        ItemMeta button4Meta = button4.getItemMeta();
        button4Meta.setDisplayName("4번 버튼");
        button4.setItemMeta(button4Meta);
        BUTTON4 = button4;

        INVENTORY_MODELING_ITEM = new ItemStack(Material.GOLD_HOE, 1, (short)16);

        // 레시피 추가
        NamespacedKey invModelingKey = new NamespacedKey(ServerCore.getInstance(), "inventory_modeling_item");
        ShapelessRecipe invModelingRecipe = new ShapelessRecipe(invModelingKey, INVENTORY_MODELING_ITEM);
        invModelingRecipe.addIngredient(1, BUTTON1.getType());
        invModelingRecipe.addIngredient(1, BUTTON2.getType());
        invModelingRecipe.addIngredient(1, BUTTON3.getType());
        invModelingRecipe.addIngredient(1, BUTTON4.getType());
        Bukkit.addRecipe(invModelingRecipe);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Bukkit.getScheduler().runTaskLater(ServerCore.getInstance(), () -> {
            addButtonToCraftingTable(p);
        }, 1L);
    }

    // NOTE: 인벤을 여는건 감지가 안되는데, 닫는건 감지가 됨.
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        InventoryView view = e.getView();
        if (isPlayerCraftingInv(view)) {
            view.getTopInventory().clear();
            // 조합대에 다시 아이템 넣어주기
            Bukkit.getScheduler().runTaskLater(ServerCore.getInstance(), () -> { // 1틱안에 다른 인벤을 열거나, 죽었을 시 추후의 onRespawn/onClose에서 처리하게 돼 있음.
                addButtonToCraftingTable(p);
            }, 1L);
        }
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        addButtonToCraftingTable(e.getPlayer());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        InventoryView view = e.getView();
        if (isPlayerCraftingInv(view) && e.getClickedInventory() != e.getWhoClicked().getInventory()) {
            if (e.getSlot() < 5) {
                e.setCancelled(true);
            }
        }
    }



    private void addButtonToCraftingTable(Player p) {
        InventoryView view = p.getOpenInventory();
        if (!p.isDead() && isPlayerCraftingInv(view)) { // 죽을 때 inventoryClose가 실행돼서 isDead도 체크해야 함
            view.setItem(1, BUTTON1);
            view.setItem(2, BUTTON2);
            view.setItem(3, BUTTON3);
            view.setItem(4, BUTTON4);
        }
    }

    private static boolean isPlayerCraftingInv(InventoryView view) {
        return view.getTopInventory().getSize() == PLAYER_CRAFT_INV_SIZE;
    }



    /*
     버그 방지
    */
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        ItemStack is = e.getEntity().getItemStack();
        if (is.equals(BUTTON1) || is.equals(BUTTON2) || is.equals(BUTTON3) || is.equals(BUTTON4) || is.equals(INVENTORY_MODELING_ITEM)) {
            // TODO: 아이템 스폰 취소 로그 추가
            Bukkit.getServer().broadcastMessage("§4[심각] BUTTON 드랍이 시도됨.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemAdded(InventoryItemChangedEvent e) {
        ItemStack is = e.getItemStack();
        if (is.equals(BUTTON1) || is.equals(BUTTON2) || is.equals(BUTTON3) || is.equals(BUTTON4) || is.equals(INVENTORY_MODELING_ITEM)) {
            // TODO: 아이템 획득 취소 로그 추가
            Bukkit.getServer().broadcastMessage("§4[심각] " + e.getPlayer().getDisplayName() + "§4에게서 BUTTON 획득이 시도됨.");
            e.getPlayer().getInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
        }
    }
}

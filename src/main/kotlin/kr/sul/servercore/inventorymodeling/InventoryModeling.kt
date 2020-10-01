package kr.sul.servercore.inventorymodeling

import kr.sul.servercore.ServerCore
import kr.sul.servercore.inventoryevent.InventoryItemChangedEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe
import java.util.logging.Level

// 원본 코드 출처 : https://github.com/AgentTroll/static-player-inv/blob/master/src/main/java/com/gmail/woodyc40/staticplayerinv/Main.java
// NOTE: 패킷으로 조합대에 템을 넣는 방법은 p.updateInventory()를 하면 아이템이 다 날아감. 근데 1초마다 자동 update되는건 안그럼.
// NOTE: 서버를 닫기 전 Kick을 하면 상관없는데, 유저를 추방하지 않고 닫아버리면 아이템이 드랍됨. -> 서버에서 나갈 때 onClose()가 호출됨.
// NOTE: onClose()에 의해서 아이템이 삭제되고, 1틱뒤에 아이템이 다시 설정되려고 하는데 유저는 이미 나갔으므로 무시됨.
object InventoryModeling : Listener {
    private val INVENTORY_MODELING_ITEM: ItemStack
    private val BUTTON1: ItemStack
    private val BUTTON2: ItemStack
    private val BUTTON3: ItemStack
    private val BUTTON4: ItemStack

    init {
        // 아이템 추가
        val button1 = ItemStack(Material.GOLD_HOE)
        val button1Meta = button1.itemMeta
        button1Meta.displayName = "1번 버튼"
        button1.itemMeta = button1Meta
        BUTTON1 = button1
        val button2 = ItemStack(Material.GOLD_HOE)
        val button2Meta = button2.itemMeta
        button2Meta.displayName = "2번 버튼"
        button2.itemMeta = button2Meta
        BUTTON2 = button2
        val button3 = ItemStack(Material.GOLD_HOE)
        val button3Meta = button3.itemMeta
        button3Meta.displayName = "3번 버튼"
        button3.itemMeta = button3Meta
        BUTTON3 = button3
        val button4 = ItemStack(Material.GOLD_HOE)
        val button4Meta = button4.itemMeta
        button4Meta.displayName = "4번 버튼"
        button4.itemMeta = button4Meta
        BUTTON4 = button4
        val inventoryModelingItem = ItemStack(Material.GOLD_HOE, 1, 16.toShort())
        val inventoryModelingItemMeta = inventoryModelingItem.itemMeta
        inventoryModelingItemMeta.displayName = "§f"
        inventoryModelingItem.itemMeta = inventoryModelingItemMeta
        INVENTORY_MODELING_ITEM = inventoryModelingItem

        // 레시피 추가
        val invModelingKey: NamespacedKey = NamespacedKey(ServerCore.instance, "inventory_modeling_item")
        val invModelingRecipe = ShapelessRecipe(invModelingKey, INVENTORY_MODELING_ITEM)
        invModelingRecipe.addIngredient(1, BUTTON1.type)
        invModelingRecipe.addIngredient(1, BUTTON2.type)
        invModelingRecipe.addIngredient(1, BUTTON3.type)
        invModelingRecipe.addIngredient(1, BUTTON4.type)
        if (Bukkit.getRecipesFor(inventoryModelingItem) == null || Bukkit.getRecipesFor(inventoryModelingItem).size == 0) {
            Bukkit.addRecipe(invModelingRecipe)
        }
    }



    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        Bukkit.getScheduler().runTaskLater(ServerCore.instance, { addButtonToCraftingTable(e.player) }, 1L)
    }

    // NOTE: 인벤을 여는건 감지가 안되는데, 닫는건 감지가 됨.
    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        val p = e.player as Player
        val view = e.view
        if (isPlayerCraftingInv(view)) {
            view.topInventory.clear()
            // 조합대에 다시 아이템 넣어주기
            Bukkit.getScheduler().runTaskLater(ServerCore.instance, { // 1틱안에 다른 인벤을 열거나, 죽었을 시 추후의 onRespawn/onClose에서 처리하게 돼 있음.
                addButtonToCraftingTable(p)
            }, 1L)
        }
    }

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        addButtonToCraftingTable(e.player)
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val view = e.view
        if (isPlayerCraftingInv(view) && e.clickedInventory !== e.whoClicked.inventory) { // 크래프팅 테이블 클릭시, 좌: container.crafting, 우: container.inventory
            if (e.slot in 0..4) {
                e.isCancelled = true
            }
        }
    }

    private fun addButtonToCraftingTable(p: Player) {
        val view = p.openInventory
        if (!p.isDead && isPlayerCraftingInv(view)) { // 죽을 때 inventoryClose가 실행돼서 isDead도 체크해야 함
            view.setItem(1, BUTTON1)
            view.setItem(2, BUTTON2)
            view.setItem(3, BUTTON3)
            view.setItem(4, BUTTON4)
        }
    }

    /*
     버그 방지
    */
    // FIXME: 플레이어가 정상적으로 나가지 않고, '현재 연결은 원격 호스트에 의해 강제로 끊겼습니다.'(인터넷/크래시?) 로 나가게 될 경우 아이템을 드랍하게됨.
    @EventHandler
    fun removeButtonTryingToDrop(e: ItemSpawnEvent) {
        val item = e.entity.itemStack
        if (item.isSimilar(BUTTON1) || item.isSimilar(BUTTON2) || item.isSimilar(BUTTON3) || item.isSimilar(BUTTON4) || item.isSimilar(INVENTORY_MODELING_ITEM)) {
            // TODO: 아이템 스폰 취소 로그 추가
            for (p in Bukkit.getServer().onlinePlayers) {
                if (p.isOp) {
                    p.sendMessage("§4[심각] BUTTON 드랍이 시도됨.")
                }
            }
            Bukkit.getServer().logger.log(Level.WARNING, "§4[심각] BUTTON 드랍이 시도됨.")
            e.isCancelled = true
        }
    }

    @EventHandler
    fun preventToObtainButton(e: InventoryItemChangedEvent) {
        val item = e.newItemStack
        if (item.isSimilar(BUTTON1) || item.isSimilar(BUTTON2) || item.isSimilar(BUTTON3) || item.isSimilar(BUTTON4) || item.isSimilar(INVENTORY_MODELING_ITEM)) {
            // TODO: 아이템 획득 취소 로그 추가
            for (p in Bukkit.getServer().onlinePlayers) {
                if (p.isOp) {
                    p.sendMessage("§4${e.player.displayName} §4에게서 BUTTON 획득이 시도됨.")
                }
            }
            Bukkit.getServer().logger.log(Level.WARNING, "§4${e.player.displayName} §4에게서 BUTTON 획득이 시도됨.")
            e.player.inventory.setItem(e.slot, ItemStack(Material.AIR))
        }
    }

    private const val PLAYER_CRAFT_INV_SIZE = 5
    private fun isPlayerCraftingInv(view: InventoryView): Boolean {
        return view.topInventory.size == PLAYER_CRAFT_INV_SIZE
    }
}
package kr.sul.servercore.inventoryevent

import kr.sul.servercore.util.InventorySlotConverterUtil
import kr.sul.servercore.util.UniqueIdAPI
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerListener
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import java.util.*

object InventoryItemListener : Listener {
    var clonedHeldItemOfPlayers = HashMap<Player, ItemStack>()

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(e: PlayerJoinEvent) {
        val ep = (e.player as CraftPlayer).handle
        ep.containerMenu.addSlotListener(object : ContainerListener {
            val p = e.player

            override fun slotChanged(
                container: AbstractContainerMenu,
                nmsSlot: Int,
                nmsIs: net.minecraft.world.item.ItemStack
            ) {
                val newSlot = InventorySlotConverterUtil.nmsSlotToSpigotSlot(nmsSlot)
                val newIs = if (p.inventory.getItem(newSlot) != null) p.inventory.getItem(newSlot)!! else ItemStack(Material.AIR)  // p.itemInMainHand와 다르다는걸 주의!

                // "슬롯을 바꾸지 않은 채로" 손에 든 아이템이 바꼈을 때. 아이템 정보 바뀌는건 제외(UID 비교. UID가 없다면 중복되게 호출될 수 있음)
                if (newSlot == p.inventory.heldItemSlot) {
                    val clonedPreviousMainIs = clonedHeldItemOfPlayers[p] ?: ItemStack(Material.AIR)
                    val bHeldItemIsChangedToAnother = !(UniqueIdAPI.hasUniqueID(clonedPreviousMainIs) && UniqueIdAPI.hasUniqueID(newIs)
                            && UniqueIdAPI.getUniqueID(clonedPreviousMainIs) == UniqueIdAPI.getUniqueID(newIs))
                    // true: 이전 아이템과 UID가 중복이 되지 않음(=다른 아이템으로 바꼈을 때) / UID가 없는 경우

                    if (bHeldItemIsChangedToAnother) {
                        Bukkit.getPluginManager().callEvent(PlayerHeldItemIsChangedToAnotherEvent(p, clonedPreviousMainIs, newIs, newSlot)) // EVENT: Call PlayerHeldItemChangeEvent
                        clonedHeldItemOfPlayers[p] = newIs.clone()

                        Bukkit.getPluginManager().callEvent(InventoryItemChangedEvent(p, newSlot, newIs)) // EVENT: Call InventoryItemChangedEvent
                    }
                } else {
                    if (newSlot in 41..45) return  // 인벤토리 닫을 때 41~45가 계속 AIR로 바뀜. 그리고 걍 조합칸은 필요없음.
                    Bukkit.getPluginManager().callEvent(InventoryItemChangedEvent(p, newSlot, newIs)) // EVENT: Call InventoryItemChangedEvent
                }
            }

            override fun dataChanged(handler: AbstractContainerMenu, property: Int, value: Int) {
            }
        })
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerItemHeld(e: PlayerItemHeldEvent) {
        if (e.isCancelled) return
        val p = e.player
        val clonedPreviousIs = e.player.inventory.getItem(e.previousSlot)?.clone() ?: ItemStack(Material.AIR) // clone()은 일부러 a()에서 PreviousItem ItemMeta 수정 못하는걸 동일시 하기 위해서 넣었음.
        val newIs = if (p.inventory.getItem(e.newSlot) != null) e.player.inventory.getItem(e.newSlot)!! else ItemStack(Material.AIR)
        if (clonedPreviousIs.type == Material.AIR && newIs.type == Material.AIR) return
        Bukkit.getPluginManager().callEvent(PlayerHeldItemIsChangedToAnotherEvent(p, clonedPreviousIs, newIs, e.newSlot))
        clonedHeldItemOfPlayers[p] = newIs.clone() // NOTE: Drop이벤트가 취소되던 말던간에 버킷이 일단 ItemStack을 AIR로 바꿔서, .clone해서 저장해야 함.
    }

    //    // ServerCore - InventoryListene 테스트 코드 (= GUI가 열린 상태에서의 아이템 변화 감지)
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
    fun onQuit(e: PlayerQuitEvent) {
        clonedHeldItemOfPlayers.remove(e.player)
    }
}
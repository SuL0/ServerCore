package kr.sul.servercore.freeze

import kr.sul.servercore.ServerCore
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.*

object FrozenPlayerListener : Listener {
    var frozenPlayer = ServerCore.frozenPlayer

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        if (frozenPlayer.getPlayerIsFrozen(e.player)) {
            frozenPlayer.setFreeze(e.player, false)
        }
    }

    /*
	 * Chat/command events
	 */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            val cmd = event.message.split(" ").toTypedArray()[0].toLowerCase()
            val player = event.player
            event.isCancelled = true
            player.sendMessage("§c§l경고! §f지금 상태에서 명령어를 사용하실 수 없습니다.")
        }
    }

    /*
	 * Movement events
	 */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            val from = event.from
            val to = event.to ?: return

            // 떨어지는 것은 허용
            if (from.blockX == to.blockX && from.blockZ == to.blockZ && from.y - to.y >= 0) {
                return
            }
            from.y = Math.floor(to.y)
            event.to = from
        }
    }

    //	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    //	public void onPlayerRespawn(PlayerRespawnEvent event) {
    //		event.setRespawnLocation(spawn);
    //	}
    /*
	 * Entity/block interaction events
	 */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerInteractAtEntity(event: PlayerInteractAtEntityEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onDamageEvent(event: EntityDamageEvent) {
        if (event.entity is Player) {
            if (frozenPlayer.getPlayerIsFrozen(event.entity as Player)) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerShear(event: PlayerShearEntityEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerFish(event: PlayerFishEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerBedEnter(event: PlayerBedEnterEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerEditBook(event: PlayerEditBookEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onSignChange(event: SignChangeEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    /*
	 * Inventory interactions
	 */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerPickupItem(event: PlayerPickupItemEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerHeldItem(event: PlayerItemHeldEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerConsumeItem(event: PlayerItemConsumeEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerInventoryOpen(event: InventoryOpenEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.player.uniqueId)) {
            val player = event.player
            event.isCancelled = true

            /*
			 * @note little hack cause InventoryOpenEvent cannot be cancelled for
			 * real, cause no packet is sent to server by client for the main inv
			 */Bukkit.getScheduler().scheduleSyncDelayedTask(ServerCore.instance, { player.closeInventory() }, 1)
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerInventoryClick(event: InventoryClickEvent) {
        if (frozenPlayer.getPlayerIsFrozen(event.whoClicked as Player)) {
            event.isCancelled = true
        }
    }
}
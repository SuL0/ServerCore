package me.sul.servercore.freeze;

import me.sul.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class FreezedPlayerListener implements Listener {
	private FreezePlayer freezePlayer;

	public FreezedPlayerListener() {
		freezePlayer = ServerCore.getFreezePlayer();
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (freezePlayer.getPlayerIsFreezed(e.getPlayer())) {
			freezePlayer.setFreeze(e.getPlayer(), false);
		}
	}
	
	
	/*
	 * Chat/command events
	 */

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			String cmd = event.getMessage().split(" ")[0].toLowerCase();
			final Player player = event.getPlayer();
			event.setCancelled(true);
			player.sendMessage("§c§l경고! §f지금 상태에서 명령어를 사용하실 수 없습니다.");
		}
	}

	/*
	 * Movement events
	 */

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			Location from = event.getFrom();
			Location to = event.getTo();
			if (to == null) {
				return;
			}
			
			// 떨어지는 것은 허용
			if (from.getBlockX() == to.getBlockX()
				&& from.getBlockZ() == to.getBlockZ()
				&& from.getY() - to.getY() >= 0) {
				return;
			}

			from.setY(Math.floor(to.getY()));
			event.setTo(from);
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
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onDamageEvent(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (freezePlayer.getPlayerIsFreezed((Player)event.getEntity())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerShear(PlayerShearEntityEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerFish(PlayerFishEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerEditBook(PlayerEditBookEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onSignChange(SignChangeEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	/*
	 * Inventory interactions
	 */

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerHeldItem(PlayerItemHeldEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerInventoryOpen(InventoryOpenEvent event) {
		if (freezePlayer.getPlayerIsFreezed(event.getPlayer().getUniqueId())) {
			final HumanEntity player = event.getPlayer();
			event.setCancelled(true);
	
			/*
			 * @note little hack cause InventoryOpenEvent cannot be cancelled for
			 * real, cause no packet is sent to server by client for the main inv
			 */
			Bukkit.getScheduler().scheduleSyncDelayedTask(ServerCore.getInstance(), player::closeInventory, 1);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerInventoryClick(InventoryClickEvent event) {
		if (freezePlayer.getPlayerIsFreezed((Player)event.getWhoClicked())) {
			event.setCancelled(true);
		}
	}
}

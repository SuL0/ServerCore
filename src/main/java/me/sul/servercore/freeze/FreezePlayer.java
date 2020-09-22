package me.sul.servercore.freeze;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.sul.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class FreezePlayer {
	private final ArrayList<UUID> freezedPlayerList = new ArrayList<UUID>();
	
	public FreezePlayer getFreezePlayer() {
		return this;
	}

	public void setFreeze(Player p, boolean b) {
		UUID pUUID = p.getUniqueId();
		if (b) {
			if (!freezedPlayerList.contains(pUUID)) {
				freezedPlayerList.add(pUUID);
			}
		} else {
			if (freezedPlayerList.contains(pUUID)) {
				freezedPlayerList.remove(pUUID);
				ServerCore.getInstance().getServer().getPluginManager().callEvent(new PlayerUnfreezeEvent(p));
			}
		}
	}

	
	public boolean getPlayerIsFreezed(UUID pUUID) {
		return freezedPlayerList.contains(pUUID);
	}
	public boolean getPlayerIsFreezed(OfflinePlayer p) {
		return getPlayerIsFreezed(p.getUniqueId());
	}
	public boolean getPlayerIsFreezed(Player p) {
		return getPlayerIsFreezed(p.getUniqueId());
	}
	
	public List<Player> getOnlinePlayersExceptFreezed() {
		List<Player> playerList = new ArrayList<Player>(ServerCore.getInstance().getServer().getOnlinePlayers());
		for (int i=0 ; i < playerList.size() ; i++) {
			if (getPlayerIsFreezed(playerList.get(i))) {
				playerList.remove(i);
			}
		}
		return playerList;
	}
	public List<Player> getOnlinePlayersExceptFreezed(String worlds) {
		List<Player> playerList = new ArrayList<Player>(ServerCore.getInstance().getServer().getOnlinePlayers());
		for (int i=0 ; i < playerList.size() ; i++) {
			if (getPlayerIsFreezed(playerList.get(i))) {
				playerList.remove(i);
			}
		}
		
		List<World> worldList = new ArrayList<World>();
		worlds = worlds.replaceAll(" ", "");
		for (String world : worlds.split(",")) {
			if (Bukkit.getWorld(world) != null)
				worldList.add(Bukkit.getWorld(world));
		}

		for (int i=0 ; i < playerList.size() ; i++) {
			Player p = playerList.get(i);
			if (!worldList.contains(p.getWorld())) {
				playerList.remove(i);
			}
		}
		return playerList;
	}


}

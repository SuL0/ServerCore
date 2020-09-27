package kr.sul.servercore.freeze

import kr.sul.servercore.ServerCore
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.*

object FrozenPlayer {
    private val frozenPlayerList = ArrayList<UUID>()

    fun setFreeze(p: Player, b: Boolean) {
        val pUUID = p.uniqueId
        if (b) {
            if (!frozenPlayerList.contains(pUUID)) {
                frozenPlayerList.add(pUUID)
            }
        } else {
            if (frozenPlayerList.contains(pUUID)) {
                frozenPlayerList.remove(pUUID)
                ServerCore.instance.server.pluginManager.callEvent(PlayerUnfrozenEvent(p))
            }
        }
    }

    fun getPlayerIsFrozen(pUUID: UUID): Boolean {
        return frozenPlayerList.contains(pUUID)
    }
    fun getPlayerIsFrozen(p: OfflinePlayer): Boolean {
        return getPlayerIsFrozen(p.uniqueId)
    }
    fun getPlayerIsFrozen(p: Player): Boolean {
        return getPlayerIsFrozen(p.uniqueId)
    }

    val onlinePlayersExceptFrozen: List<Player>
        get() {
            val playerList = ServerCore.instance.server.onlinePlayers.toMutableList()
            for (i in playerList.indices) {
                if (getPlayerIsFrozen(playerList[i])) {
                    playerList.removeAt(i)
                }
            }
            return playerList
        }

    fun getOnlinePlayersExceptFrozen(worlds: String): List<Player> {
        var worlds = worlds
        val playerList = ServerCore.instance.server.onlinePlayers.toMutableList()
        for (i in playerList.indices) {
            if (getPlayerIsFrozen(playerList[i])) {
                playerList.removeAt(i)
            }
        }
        val worldList: MutableList<World> = ArrayList()
        worlds = worlds.replace(" ".toRegex(), "")
        for (world in worlds.split(",").toTypedArray()) {
            if (Bukkit.getWorld(world) != null) worldList.add(Bukkit.getWorld(world))
        }
        for (i in playerList.indices) {
            val p = playerList[i]
            if (!worldList.contains(p.world)) {
                playerList.removeAt(i)
            }
        }
        return playerList
    }
}
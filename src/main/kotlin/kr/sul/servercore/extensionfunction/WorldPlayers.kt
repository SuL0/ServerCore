package kr.sul.servercore.extensionfunction

import kr.sul.servercore.ServerCore
import net.citizensnpcs.api.CitizensAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

// Bukkit.getOnlinePlayers 에는 world.players 와 달리 Citizen NPC가 섞여들어가는 일은 없음
object WorldPlayers {
    private var isCitizenEnabled = false
    init {
        Bukkit.getScheduler().runTask(ServerCore.plugin) {
            if (Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
                isCitizenEnabled = true
            }
        }
    }

    /**
     * Citizen NPC가 아닌 진짜 Player 리스트만 반환함
     */
    fun World.getRealPlayers(): List<Player> {
        if (!isCitizenEnabled) {
            return this.players
        }
        val players = this.players
        val toRemove = arrayListOf<Player>()
        for (p in players) {
            for (reg in CitizensAPI.getNPCRegistries()) {
                if (reg.isNPC(p)) {
                    toRemove.add(p)
                    break
                }
            }
        }
        players.removeAll(toRemove)
        return players
    }

    fun Location.getNearbyRealPlayers(radius: Double): MutableCollection<Player> {
        if (!isCitizenEnabled) {
            return this.getNearbyPlayers(radius)!!
        }
        val realPlayersInThisWorld = this.world.getRealPlayers()
        val toReturn = arrayListOf<Player>()
        for (p in realPlayersInThisWorld) {
            if (this.distance(p.location) <= radius) {
                toReturn.add(p)
            }
        }
        return toReturn
    }
    fun World.getRealPlayersExcept(p: Player): List<Player> {
        return getRealPlayers().filter { it != p }
    }
}
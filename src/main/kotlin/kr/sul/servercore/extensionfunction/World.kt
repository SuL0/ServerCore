package kr.sul.servercore.extensionfunction

import kr.sul.servercore.ServerCore
import net.citizensnpcs.api.CitizensAPI
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player

object World {
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
        players.forEach { p ->
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
    fun World.getRealPlayersExcept(p: Player): List<Player> {
        return getRealPlayers().filter { it != p }
    }
}
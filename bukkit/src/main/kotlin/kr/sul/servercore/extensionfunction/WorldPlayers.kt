package kr.sul.servercore.extensionfunction

import kr.sul.servercore.ServerCore
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.npc.ai.NPCHolder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
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
        // TODO 테스트 필요
        val players = this.players//.removeIf { it is NPCHolder || (it as CraftPlayer).handle is NPCHolder }
        val toRemove = arrayListOf<Player>()
        for (p in players) {
            if (p is NPCHolder) {
                Bukkit.broadcastMessage("1")
                toRemove.add(p)
            }
            if ((p as CraftPlayer).handle is NPCHolder) {  // reg.isNPC() 의 코드에서 핵심만 가져옴
                Bukkit.broadcastMessage("2")
                toRemove.add(p)
            }
        }
        players.removeAll(toRemove)
        return players
    }

    fun Location.getNearbyRealPlayers(radius: Double): MutableCollection<Player> {
        return getNearbyPlayers(radius, radius, radius)
    }
    fun Location.getNearbyRealPlayers(xzRadius: Double, yRadius: Double): MutableCollection<Player> {
        return getNearbyPlayers(xzRadius, yRadius, xzRadius)
    }
    fun Location.getNearbyRealPlayers(xRadius: Double, yRadius: Double, zRadius: Double, predicate: ((Player)-> Boolean)? = null): MutableCollection<Player> {
        if (!isCitizenEnabled) {
            return this.getNearbyPlayers(xRadius, yRadius, zRadius)!!
        }
        val realPlayersInThisWorld = this.world.getRealPlayers()

        if (predicate == null) {
            return this.getNearbyPlayers(xRadius, yRadius, zRadius) { p ->
                realPlayersInThisWorld.contains(p)
            }
        }
        return this.getNearbyPlayers(xRadius, yRadius, zRadius) { p ->
            realPlayersInThisWorld.contains(p) && predicate.invoke(p)
        }
    }

    fun World.getRealPlayersExcept(p: Player): List<Player> {
        return getRealPlayers().filter { it != p }
    }
}
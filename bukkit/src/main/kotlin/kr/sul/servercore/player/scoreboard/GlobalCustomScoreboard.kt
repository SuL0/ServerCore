package kr.sul.servercore.player.scoreboard

import org.bukkit.entity.Player

interface GlobalCustomScoreboard: CustomScoreboard {
    fun addPlayer(p: Player)
    fun removePlayer(p: Player)
}
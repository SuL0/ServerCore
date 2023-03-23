package kr.sul.servercore.player.scoreboard

import org.bukkit.entity.Player

interface PerPlayerCustomScoreboard: CustomScoreboard {
    val p: Player
    fun activate()
    fun stop()
}
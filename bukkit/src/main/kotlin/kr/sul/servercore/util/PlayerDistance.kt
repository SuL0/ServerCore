package kr.sul.servercore.util

import org.bukkit.entity.Player

object PlayerDistance {
    fun Player.distance(p: Player): Double {
        return this.location.distance(p.location)
    }
}
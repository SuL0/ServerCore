package kr.sul.servercore.util

import org.bukkit.Bukkit
import org.bukkit.Location

object SpawnLocation {
    private const val WORLD_NAME = "iscraft_spawn"
    private const val X = (-215).toDouble()
    private const val Y = 64.toDouble()
    private const val Z = 240.5.toDouble()

    fun get(): Location {
        return Location(Bukkit.getWorld(WORLD_NAME), X, Y, Z, 90.toFloat(), 3.toFloat())
    }
}
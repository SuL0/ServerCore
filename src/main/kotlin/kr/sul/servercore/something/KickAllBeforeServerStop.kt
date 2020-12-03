package kr.sul.servercore.something

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.server.ServerCommandEvent

object KickAllBeforeServerStop : Listener {
    private const val STOP_MESSAGE = "§c§l- ISCRAFT -\n\n\n§f서버가 곧 재시작됩니다."

    @EventHandler
    fun onPlayerSayStop(e: PlayerCommandPreprocessEvent) {
        if (!e.player.isOp || e.isCancelled) return
        if (e.message.equals("/stop", true)) {
            e.isCancelled = true
            for (p in Bukkit.getServer().onlinePlayers) {
                p.kickPlayer(STOP_MESSAGE)
            }
            Bukkit.shutdown()
        }
    }

    @EventHandler
    fun onConsoleSayStop(e: ServerCommandEvent) {
        if (e.isCancelled) return
        if (e.command.equals("stop", true)) {
            e.isCancelled = true
            for (p in Bukkit.getServer().onlinePlayers) {
                p.kickPlayer(STOP_MESSAGE)
            }
            Bukkit.shutdown()
        }
    }
}
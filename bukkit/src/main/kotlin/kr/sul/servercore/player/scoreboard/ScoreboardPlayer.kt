package kr.sul.servercore.player.scoreboard

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

// 스코어보드 갈아 끼우기 위한 클래스
class ScoreboardPlayer(val p: Player) {
    var currentScoreboard: CustomScoreboard? = null
        private set

    /**
     * 이전 Scoreboard(nullable)를 해당 Scoreboard 로 교체한다
     */
    fun applyScoreboard(scoreboard: CustomScoreboard) {
        removeScoreboard()

        currentScoreboard = scoreboard
        if (currentScoreboard is GlobalCustomScoreboard) {
            (currentScoreboard as GlobalCustomScoreboard).addPlayer(p)
        } else if (currentScoreboard is PerPlayerCustomScoreboard){
            (currentScoreboard as PerPlayerCustomScoreboard).activate()
        }
    }
    fun removeScoreboard() {
        if (currentScoreboard is GlobalCustomScoreboard) {
            (currentScoreboard as GlobalCustomScoreboard).removePlayer(p)
        } else if (currentScoreboard is PerPlayerCustomScoreboard){
            (currentScoreboard as PerPlayerCustomScoreboard).stop()
        }
        currentScoreboard = null
    }


    object Mgr: Listener {
        private val scoreboardPlayerList = hashMapOf<Player, ScoreboardPlayer>()

        @EventHandler(priority = EventPriority.LOWEST)
        fun onJoin(e: PlayerJoinEvent) {
            scoreboardPlayerList[e.player] = ScoreboardPlayer(e.player)
        }
        @EventHandler(priority = EventPriority.HIGHEST)
        fun onQuit(e: PlayerQuitEvent) {
            scoreboardPlayerList.remove(e.player)
        }
        fun get(p: Player): ScoreboardPlayer? {
            return scoreboardPlayerList[p]
        }
    }
}
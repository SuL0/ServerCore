package kr.sul.servercore.something

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object TakeAwayPermissionIfNotOp : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!e.player.isOp) {
            // Attachment 붙이고 pPerm으로 개 지랄하다간 이유는 뭔지 몰라도 안되고, 오류남. (아마 Attachment 붙일 때 Plugin을 LuckPerms로 안 해줘서 그런 것 같긴한데)
            if (e.player.hasPermission("*")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${e.player.name} permission unset *")
            }
            if (e.player.hasPermission("op.op")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${e.player.name} permission unset op.op")
            }

            // TODO: DB에 로그 추가
        }
    }
}
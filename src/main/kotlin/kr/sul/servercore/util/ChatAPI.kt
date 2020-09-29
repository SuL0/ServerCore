package kr.sul.servercore.util

import org.bukkit.entity.Player

object ChatAPI {
    @JvmStatic
    fun sendUnknownCommandMessage(p: Player) {
        p.sendMessage("§a§lHELP: &f알 수 없는 명령어입니다.")
    }

    @JvmStatic
    fun sendInsufficientPermissionMessage(p: Player) {
        p.sendMessage("§c§lHELP: &c당신에게는 권한이 없습니다!")
    }
}
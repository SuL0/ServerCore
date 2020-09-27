package kr.sul.servercore.util

import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ChatAPI {
    fun format(string: String?): String {
        return ChatColor.translateAlternateColorCodes('&', string)
    }

    fun sendUnknownCommandMessage(p: Player) {
        p.sendMessage(format("&c&lHELP: &f알 수 없는 명령어입니다."))
    }

    fun sendInsufficientPermissionMessage(p: Player) {
        p.sendMessage(format("&c&lHELP: &c당신에게는 권한이 없습니다!"))
    }
}
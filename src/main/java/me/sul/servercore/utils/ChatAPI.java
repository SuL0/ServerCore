package me.sul.servercore.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatAPI {
    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public static void sendUnknownCommandMessage(Player p) {
        p.sendMessage(ChatAPI.format("&c&lHELP: &f알 수 없는 명령어입니다."));
    }
    public static void sendInsufficientPermissionMessage(Player p) {
        p.sendMessage(ChatAPI.format("&c&lHELP: &c당신에게는 권한이 없습니다!"));
    }
}

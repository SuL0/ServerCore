package me.sul.servercore.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatAPI {
    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public static void sendUnknownCommandMessage(Player p) {
        p.sendMessage(ChatAPI.format("&c&lHELP: &f�� �� ���� ��ɾ��Դϴ�."));
    }
    public static void sendInsufficientPermissionMessage(Player p) {
        p.sendMessage(ChatAPI.format("&c&lHELP: &c��ſ��Դ� ������ �����ϴ�!"));
    }
}

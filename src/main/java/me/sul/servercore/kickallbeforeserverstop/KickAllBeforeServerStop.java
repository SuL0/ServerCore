package me.sul.servercore.kickallbeforeserverstop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class KickAllBeforeServerStop implements Listener {
    private final String STOP_MESSAGE = "§c§l- ISCRAFT -\n\n\n§f서버가 곧 재시작됩니다.";

    // NOTE: 펄미션이 없어도 이벤트가 실행되고, 펄미션의 여부를 알 수 없으니 주의.
    @EventHandler
    public void onPlayerSayStop(PlayerCommandPreprocessEvent e) {
        if (!e.getPlayer().isOp()) return;
        if (e.getMessage().equalsIgnoreCase("/stop")) {
            e.setCancelled(true);
            for (Player p: Bukkit.getServer().getOnlinePlayers()) {
                p.kickPlayer(STOP_MESSAGE);
            }
            Bukkit.shutdown();
        }
    }

    @EventHandler
    public void onConsoleSayStop(ServerCommandEvent e) {
        if (e.getCommand().equalsIgnoreCase("stop")) {
            e.setCancelled(true);
            for (Player p: Bukkit.getServer().getOnlinePlayers()) {
                p.kickPlayer(STOP_MESSAGE);
            }
            Bukkit.shutdown();
        }
    }
}
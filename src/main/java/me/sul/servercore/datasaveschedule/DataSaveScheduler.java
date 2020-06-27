package me.sul.servercore.datasaveschedule;

import me.sul.servercore.ServerCore;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class DataSaveScheduler implements Listener {
    private static final int SAVE_INTERVAL = 5;  // min

    public DataSaveScheduler() {
        new BukkitRunnable() {
            public void run() {
                ServerCore.getInstance().getServer().getPluginManager().callEvent((Event)new DataSaveScheduleEvent(false));
                ServerCore.getInstance().getLogger().log(Level.INFO, "서버 저장시간 : " + SAVE_INTERVAL + "m");
            }
        }.runTaskTimer((Plugin) ServerCore.getInstance(), SAVE_INTERVAL * 1200, SAVE_INTERVAL * 1200);
    }
}

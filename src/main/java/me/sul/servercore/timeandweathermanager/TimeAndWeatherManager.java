package me.sul.servercore.timeandweathermanager;

import me.sul.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class TimeAndWeatherManager {
    // 06:00 이 0틱. 1시간당 1000틱. 전체 틱은 24000.
    // 08:00 ~ 16:00 으로 고정
    public TimeAndWeatherManager() {
        Bukkit.getScheduler().runTaskTimer(ServerCore.getInstance(), () -> {
            for (World world : Bukkit.getServer().getWorlds()) {
                long time = world.getTime();
                if (!(time >= 2000 && time <= 10000)) {
                    world.setTime(2000);
                }
                if (world.hasStorm()) world.setStorm(false);
                if (world.isThundering()) world.setThundering(false);
            }
        }, 0L, 40*20L);
    }
}

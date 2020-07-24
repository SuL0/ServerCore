package me.sul.servercore.timemanager;

import me.sul.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class TimeManager {
    // 06:00 이 0틱. 1시간당 1000틱. 전체 틱은 24000.
    // 08:00 ~ 16:00 으로 고정
    public TimeManager() {
        Bukkit.getScheduler().runTaskTimer(ServerCore.getInstance(), () -> {
            for (World world : Bukkit.getServer().getWorlds()) {
                long time = world.getTime();
                if (!(time >= 2000 && time <= 10000)) {
                    world.setTime(2000);
                }
            }
        }, 0L, 40*20L);
    }
}

package me.sul.servercore.worldmanager;

import me.sul.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldManager implements Listener {
    public WorldManager() {
        // 월드 GameRule 설정
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getGameRuleValue("announceAdvancements").equals("true")) {
                world.setGameRuleValue("announceAdvancements", "false");
            }
        }

        // 월드 시간 고정 (08:00 ~ 16:00)
        // 06:00 이 0틱. 1시간당 1000틱. 전체 틱은 24000.
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

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.isCancelled()) return;
        if (e.toWeatherState()) { // true if the weather is being set to raining, false otherwise
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            e.getDrops().clear();
        }
        e.setDroppedExp(0);
    }
}

package kr.sul.servercore.something

import kr.sul.servercore.ServerCore.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable


object BossBarTimer {
    fun setTimer(p: Player, time: Int, bossBarTitleLambda: (Int) -> (String), barColor: BarColor, barStyle: BarStyle, vararg barFlags: BarFlag) {
        object : BukkitRunnable() {
            var leftTime = time +1
            var previousBossBar: BossBar? = null
            override fun run() {
                if (!p.isOnline) { this.cancel(); return }
                previousBossBar?.removeAll()
                if (--leftTime == 0) {
                    this.cancel()
                    return
                }
                val bossBar = Bukkit.createBossBar(bossBarTitleLambda.invoke(leftTime), barColor, barStyle, *barFlags)
                bossBar.progress = leftTime / time.toDouble()
                bossBar.addPlayer(p)
                previousBossBar = bossBar
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }
}
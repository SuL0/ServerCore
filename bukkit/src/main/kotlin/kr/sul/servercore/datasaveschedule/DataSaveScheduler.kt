package kr.sul.servercore.datasaveschedule

import kr.sul.servercore.ServerCore
import org.bukkit.scheduler.BukkitRunnable
import java.util.logging.Level

object DataSaveScheduler {
    private const val SAVE_INTERVAL = 5 // min

    init {
        object : BukkitRunnable() {
            override fun run() {
                ServerCore.instance.server.pluginManager.callEvent(DataSaveScheduleEvent(false))
                ServerCore.instance.logger.log(Level.INFO, "서버 저장시간 : ${SAVE_INTERVAL}m")
            }
        }.runTaskTimer(ServerCore.instance, SAVE_INTERVAL * 1200L, SAVE_INTERVAL * 1200L)
    }
}
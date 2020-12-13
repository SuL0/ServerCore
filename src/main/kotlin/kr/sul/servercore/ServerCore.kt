package kr.sul.servercore

import kr.sul.servercore.command.KillAllCommand
import kr.sul.servercore.command.NbtViewCommand
import kr.sul.servercore.datasaveschedule.DataSaveCommand
import kr.sul.servercore.datasaveschedule.DataSaveScheduleEvent
import kr.sul.servercore.datasaveschedule.DataSaveScheduler
import kr.sul.servercore.freeze.FrozenPlayer
import kr.sul.servercore.freeze.FrozenPlayerListener
import kr.sul.servercore.inventoryevent.InventoryItemListener
import kr.sul.servercore.something.InventoryModeling
import kr.sul.servercore.something.KickAllBeforeServerStop
import kr.sul.servercore.something.TakeAwayPermissionIfNotOp
import kr.sul.servercore.util.ObjectInitializer
import kr.sul.servercore.util.UptimeBasedOnTick
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class ServerCore : JavaPlugin() {
    companion object {
        lateinit var plugin: Plugin private set
        lateinit var instance: ServerCore private set
        val frozenPlayer = FrozenPlayer
    }

    override fun onEnable() {
        plugin = this as Plugin
        instance = this
        registerClasses()
    }

    private fun registerClasses() {
        registerDataSaveSchedule()
        Bukkit.getPluginManager().registerEvents(FrozenPlayerListener, this)
        Bukkit.getPluginManager().registerEvents(InventoryItemListener, this)
        Bukkit.getPluginManager().registerEvents(InventoryModeling, this)
        Bukkit.getPluginManager().registerEvents(KickAllBeforeServerStop, this)
        Bukkit.getPluginManager().registerEvents(KillAllCommand, this)
        Bukkit.getPluginManager().registerEvents(TakeAwayPermissionIfNotOp, this)
        ObjectInitializer.forceInit(UptimeBasedOnTick::class.java)
    }
    private fun registerDataSaveSchedule() {
        ObjectInitializer.forceInit(DataSaveScheduler::class.java)
        getCommand("서버저장").executor = DataSaveCommand
        getCommand("nbtview").executor = NbtViewCommand
    }

    // 이거 제대로 되는지 모르겠네.
    // onDisable() 실행되고 나서 event가 1틱 뒤에 받아질 수도 있음.
    // TODO) 이벤트가 제때 받아지는지 확인할 필요가 있음.
    // TODO) 이거 월드 저장도 해줘야되는데, 코드를 어디다가 넣어놨더라?
    override fun onDisable() {
        instance.server.pluginManager.callEvent(DataSaveScheduleEvent(false))
        instance.logger.log(Level.INFO, "[서버 종료] 서버 데이터 저장")
    }
}
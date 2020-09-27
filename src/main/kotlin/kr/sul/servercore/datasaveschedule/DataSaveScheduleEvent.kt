package kr.sul.servercore.datasaveschedule

import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class DataSaveScheduleEvent(val isServerDisabling: Boolean) : Event(), Cancellable {
    private var cancelled = false

    override fun isCancelled(): Boolean { return cancelled }
    override fun setCancelled(cancelled: Boolean) { this.cancelled = cancelled }

    override fun getHandlers(): HandlerList { return handlerList }
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
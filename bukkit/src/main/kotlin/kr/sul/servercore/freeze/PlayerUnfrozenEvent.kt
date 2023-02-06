//package kr.sul.servercore.freeze
//
//import org.bukkit.entity.Player
//import org.bukkit.event.Cancellable
//import org.bukkit.event.Event
//import org.bukkit.event.HandlerList
//
//class PlayerUnfrozenEvent(val player: Player) : Event(), Cancellable {
//    private var cancelled = false
//    override fun isCancelled(): Boolean { return cancelled }
//
//    override fun setCancelled(b: Boolean) { cancelled = b }
//
//
//    override fun getHandlers(): HandlerList { return handlerList }
//    companion object {
//        @JvmStatic
//        val handlerList = HandlerList()
//    }
//}
package kr.sul.servercore.datasaveschedule

import kr.sul.servercore.ServerCore
import kr.sul.servercore.util.ChatAPI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.logging.Level

object DataSaveCommand : CommandExecutor {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (commandSender is Player) {
            if (!commandSender.isOp()) {
                ChatAPI.sendInsufficientPermissionMessage(commandSender)
                return true
            }
        }
        ServerCore.instance.server.pluginManager.callEvent(DataSaveScheduleEvent(false))
        ServerCore.instance.logger.log(Level.INFO, "서버 데이터 즉시 저장")
        (commandSender as? Player)?.sendMessage(ChatAPI.format("&c&lSERVER: &f서버 데이터를 저장하였습니다."))
        return true
    }
}
package me.sul.servercore.datasaveschedule;

import me.sul.servercore.ServerCore;
import me.sul.servercore.utils.ChatAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.logging.Level;

public class DataSaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (!((Player)commandSender).isOp()) {
                ChatAPI.sendInsufficientPermissionMessage((Player)commandSender);
                return true;
            }
        }
        ServerCore.getInstance().getServer().getPluginManager().callEvent((Event)new DataSaveScheduleEvent(false));
        ServerCore.getInstance().getLogger().log(Level.INFO, "서버 데이터 즉시 저장");
        if (commandSender instanceof Player) {
            ((Player)commandSender).sendMessage(ChatAPI.format("&c&lSERVER: &f서버 데이터를 저장하였습니다."));
        }
        return true;
    }
}

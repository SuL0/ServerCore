package kr.sul.servercore.command

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object NbtViewCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return false
        val p = sender as Player
        if (args.isEmpty()) {
            p.sendMessage("§f/NbtView <Key1, Key2, ..>")
            p.sendMessage("§7Keys often used: Item-UniqueID")
            return true
        }
        if (p.inventory.itemInMainHand == null || p.inventory.itemInMainHand.type == Material.AIR) {
            p.sendMessage("§c손에 아이템을 들어주십시오.")
            return true
        }
        val nbtItem = NBTItem(p.inventory.itemInMainHand)
        for (nbtKey in args) {
            var modifiedNbtKey = nbtKey.replace(",", "");
            modifiedNbtKey = modifiedNbtKey.replace(" ", "")
            if (modifiedNbtKey != "") {
                printNbtValue(p, nbtItem, modifiedNbtKey)
            }
        }
        return true
    }

    private fun printNbtValue(p: Player, nbtItem: NBTItem, key: String) {
        val dataType = nbtItem.getType(key)
        val value = nbtItem.getObject(key, Any::class.java)
        p.sendMessage("- §6$key [${dataType.name}] §f: $value")
    }
}
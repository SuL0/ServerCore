package me.sul.servercore;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length == 0) {
            return true;
        }
        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("checked")) {
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            NamespacedKey key = new NamespacedKey(ServerCore.getInstance(), "Serial-Number");
            if (meta.getPersistentDataContainer().has(key, PersistentDataType.LONG)) {
                player.sendMessage("Serial-Number: " + meta.getPersistentDataContainer().get(key, PersistentDataType.LONG));
            } else {
                player.sendMessage("Serial-Number is not set");
            }
        }

        else if (args[0].equalsIgnoreCase("unchecked")) {
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            NamespacedKey key = new NamespacedKey(ServerCore.getInstance(), "Serial-Number");
            player.sendMessage("Serial-Number: " + meta.getPersistentDataContainer().get(key, PersistentDataType.LONG));
        }
        else if (args[0].equalsIgnoreCase("metadata")) {
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            player.sendMessage(meta.toString());
        }
        else if (args[0].equalsIgnoreCase("createitem")) {
            ItemStack is = new ItemStack(Material.IRON_SWORD);
            ItemMeta meta = is.getItemMeta();
            NamespacedKey key = new NamespacedKey(ServerCore.getInstance(), "Serial-Number");
            meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, (long)777);
            is.setItemMeta(meta);
            player.getInventory().addItem(is);
            player.sendMessage("Serial-Number이 777인 철 검 아이템을 지급하였습니다.");
        }
        return true;
    }
}

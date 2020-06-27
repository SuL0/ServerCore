package me.sul.servercore.serialnumber;

import me.sul.servercore.ServerCore;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SerialNumberAPI {
    private static SerialNumberAPI serialNumberAPI;

    public SerialNumberAPI() {
        serialNumberAPI = this;
    }
    public static SerialNumberAPI getAPI() {
        return serialNumberAPI;
    }

    public ItemStack carveSerialNumber(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        is.setItemMeta(carveSerialNumber(meta));
        return is;
    }
    public ItemMeta carveSerialNumber(ItemMeta meta) {
        NamespacedKey key = new NamespacedKey(ServerCore.getInstance(), "Serial-Number");
        meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, SerialNumberCounter.getInstance().getCountAndAddOne());
        return meta;
    }

    public long getSerialNumber(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        return getSerialNumber(meta);
    }
    public long getSerialNumber(ItemMeta meta) {
        NamespacedKey key = new NamespacedKey(ServerCore.getInstance(), "Serial-Number");
        return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.LONG, (long)-1);
    }
}

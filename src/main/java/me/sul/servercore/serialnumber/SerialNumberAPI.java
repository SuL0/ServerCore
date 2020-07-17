package me.sul.servercore.serialnumber;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SerialNumberAPI {
    private static final String SERIALNUMBERKEY = "SerialNumber";

    public static ItemStack carveSerialNumber(ItemStack is) {
        NBTItem nbti = new NBTItem(is);
        nbti.setString(SERIALNUMBERKEY, UUID.randomUUID().toString());
        return nbti.getItem();
    }

    public static boolean hasSerialNumber(ItemStack is) {
        if (is.getType().equals(Material.AIR)) return false;
        NBTItem nbti = new NBTItem(is);
        return nbti.hasKey(SERIALNUMBERKEY);
    }
    public static String getSerialNumber(ItemStack is) {
        NBTItem nbti = new NBTItem(is);
        return nbti.getString(SERIALNUMBERKEY);
    }
}

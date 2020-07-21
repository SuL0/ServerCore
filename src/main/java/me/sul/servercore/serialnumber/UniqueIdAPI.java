package me.sul.servercore.serialnumber;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class UniqueIdAPI {
    private static final String UNIQUEID_KEY = "UniqueID";

    public static void carveUniqueID(ItemStack is) {
        NBTItem nbti = new NBTItem(is);
        nbti.setString(UNIQUEID_KEY, UUID.randomUUID().toString());
        is.setItemMeta(nbti.getItem().getItemMeta());
    }

    public static boolean hasUniqueID(ItemStack is) {
        if (is.getType().equals(Material.AIR)) return false;
        NBTItem nbti = new NBTItem(is);
        return nbti.hasKey(UNIQUEID_KEY);
    }
    public static String getUniqueID(ItemStack is) {
        NBTItem nbti = new NBTItem(is);
        return nbti.getString(UNIQUEID_KEY);
    }
}

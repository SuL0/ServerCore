package me.sul.servercore.playertoolchangeevent;

public class InventorySlotConverterUtil {
    public static int nmsSlotToSpigotSlot(int nmsSlot) {
        int spigotSlot = nmsSlot;
        if (nmsSlot >= 5 && nmsSlot <= 8) {  // 갑옷칸
            if (nmsSlot == 5) { spigotSlot = 39; }
            else if (nmsSlot == 6) { spigotSlot = 38; }
            else if (nmsSlot == 7) { spigotSlot = 37; }
            else if (nmsSlot == 8) { spigotSlot = 36; }
        } else if (nmsSlot >= 36 && nmsSlot <= 44) {  // 1~9번 핫바칸
            spigotSlot = nmsSlot - 36;
        } else if (nmsSlot == 45) {  // offHand칸
            spigotSlot = 40;
        }
        return spigotSlot;
    }
}

package me.sul.servercore.playertoolchangeevent;

public class InventorySlotConverterUtil {
    public static int nmsSlotToSpigotSlot(int nmsSlot) {
        int spigotSlot = -1;
        if (nmsSlot >= 5 && nmsSlot <= 8) {  // ����ĭ
            if (nmsSlot == 5) { spigotSlot = 39; }
            else if (nmsSlot == 6) { spigotSlot = 38; }
            else if (nmsSlot == 7) { spigotSlot = 37; }
            else if (nmsSlot == 8) { spigotSlot = 36; }
        } else if (nmsSlot >= 36 && nmsSlot <= 44) {  // 1~9�� �ֹ�ĭ
            spigotSlot = nmsSlot - 36;
        } else if (nmsSlot == 45) {  // offHandĭ
            spigotSlot = 40;
        }
        return spigotSlot;
    }
}

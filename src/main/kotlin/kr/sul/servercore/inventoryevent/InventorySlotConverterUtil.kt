package kr.sul.servercore.inventoryevent

object InventorySlotConverterUtil {
    fun nmsSlotToSpigotSlot(nmsSlot: Int): Int {
        var spigotSlot = nmsSlot
        when (nmsSlot) {
            in 0..4 -> spigotSlot = nmsSlot + 41  // 조합대 칸. 0: Result ... -> 41: Result, 42, 43, 44, 45로 설정
            in 5..8 -> { // 갑옷칸
                when (nmsSlot) {
                    5 -> spigotSlot = 39
                    6 -> spigotSlot = 38
                    7 -> spigotSlot = 37
                    8 -> spigotSlot = 36
                }
            }
            in 36..44 -> spigotSlot = nmsSlot - 36  // 1~9번 핫바칸
            45 -> spigotSlot = 40  // offHand칸
        }
        return spigotSlot
    }
}
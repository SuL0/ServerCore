package kr.sul.servercore.util

object InventorySlotConverterUtil {
    fun nmsSlotToSpigotSlot(nmsSlot: Int): Int {
        return when (nmsSlot) {
            in 0..4 -> nmsSlot + 41  // 조합대 칸. 0: Result ... -> 41: Result, 42, 43, 44, 45로 설정
            in 5..8 -> { // 갑옷 칸
                when (nmsSlot) {
                    5 -> 39
                    6 -> 38
                    7 -> 37
                    8 -> 36
                    else -> throw Exception("$nmsSlot")
                }
            }
            in 36..44 -> nmsSlot - 36  // 1~9번 핫바 칸
            45 -> 40  // offHand 칸
            else -> nmsSlot
        }
    }

    fun spigotSlotToNmsSlot(spigotSlot: Int): Int {
        return when(spigotSlot) {
            in 41..45 -> spigotSlot - 41  // 조합대 칸
            in 36..39 -> { // 갑옷 칸
                when(spigotSlot) {
                    36 -> 8
                    37 -> 7
                    38 -> 6
                    39 -> 5
                    else -> throw Exception("$spigotSlot")
                }
            }
            in 0..8 -> spigotSlot + 36  // 1~9번 핫바 칸
            40 -> 45  // offHand 칸
            else -> spigotSlot
        }
    }
}
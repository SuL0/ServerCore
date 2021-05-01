package kr.sul.servercore.file

import kr.sul.servercore.ServerCore.Companion.plugin
import org.bukkit.Bukkit
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CustomFileUtil {

    /**
     * 기본 양식 "yyyy-MM-dd (HHmmss)" 으로 가져오기
     */
    fun getFormattedDateTime(): String {
        return getFormattedDateTime("yyyy-MM-dd (HHmmss)")
    }
    /**
     * @param customFormat e.g. "yyyy-MM-dd (HHmmss)" || "yyyy-MM-dd"
     */
    fun getFormattedDateTime(customFormat: String): String {
        val calendar = Calendar.getInstance()

        val dateFormat = SimpleDateFormat(customFormat)
        return dateFormat.format(calendar.time)
    }

    fun deleteFilesOlderThanNdays(daysBack: Int, dir: File, dontDeleteAtLeast: Int) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            val listFiles = dir.listFiles()
            val purgeTime = System.currentTimeMillis() - daysBack * 24 * 60 * 60 * 1000
            if (listFiles != null && listFiles.isNotEmpty()) {
                for (listFile in listFiles) {
                    if (listFile.lastModified() < purgeTime && listFiles.size > dontDeleteAtLeast) {
                        listFile.delete()
                    }
                }
            }
        }
    }
}
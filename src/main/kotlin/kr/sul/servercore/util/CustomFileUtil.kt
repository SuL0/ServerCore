package kr.sul.servercore.util

import java.io.File

object CustomFileUtil {
    fun deleteFilesOlderThanNdays(daysBack: Int, dir: File, dontDeleteAtLeast: Int) {
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
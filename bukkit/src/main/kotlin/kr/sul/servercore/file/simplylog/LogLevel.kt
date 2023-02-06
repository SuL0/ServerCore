package kr.sul.servercore.file.simplylog

import kr.sul.servercore.file.CustomFileUtil
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.io.File

enum class LogLevel(folderName: String, val howToStackLog: ((LogLevel, Plugin, List<String>) -> Any)?) {
    CHAT("chat", { obj, _, logs ->
        obj.waitingLogs.addAll(logs)
    }),
    INFO("info", null),
    ERROR_LOW("error/low", null),
    ERROR_NORMAL("error/normal", null),
    ERROR_CRITICAL("error/critical", null);

    val file = File("${SimplyLog.parentFolder.path}/${folderName}/${name.toLowerCase()} - ${CustomFileUtil.getFormattedDateTime()}.log")
    val waitingLogs = arrayListOf<String>()  // 로그 파일 쓰기 전 대기열 (SAVE_INTERVAL 마다 파일에 저장)

    init {
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if (!file.exists()) {
            file.createNewFile()
        }

        // 파일 청소
        CustomFileUtil.deleteFilesOlderThanNdays(30, SimplyLog.parentFolder, 30, true)
    }
}
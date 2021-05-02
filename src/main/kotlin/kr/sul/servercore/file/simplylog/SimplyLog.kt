package kr.sul.servercore.file.simplylog

import kr.sul.servercore.ServerCore.Companion.plugin
import kr.sul.servercore.file.CustomFileUtil
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.io.*

// 버킷 폴더/logs_S/...
// TODO: Test
// TODO: 현재 작성중이 아닌, 이미 작성이 끝난 이전 파일들은 init { } 에서 zip 으로 압축하기
object SimplyLog {
    val parentFolder = run {
        val rootFolder = Bukkit.getServer().worldContainer  // 버킷 있는 곳  https://www.spigotmc.org/threads/server-directory.343550/
        return@run File("${rootFolder.path}/logs_S")
    }
    private const val SAVE_INTERVAL = ((10*60)*20).toLong()


    init {
        // 폴더, 파일 생성
        if (!parentFolder.exists()) {
            parentFolder.mkdir()
        }

        // 저장 scheduler
        Bukkit.getScheduler().runTaskTimer(plugin, {
            saveAllToFile(true)
        }, SAVE_INTERVAL, SAVE_INTERVAL)
    }



    // SAVE_INTERVAL 마다 & 서버 꺼질 때 실행
    fun saveAllToFile(asAsync: Boolean) {
        val taskRunnable = Runnable {
            for (logLevel in LogLevel.values()) {
                if (logLevel.waitingLogs.isNotEmpty()) {
                    val bWriter = BufferedWriter(OutputStreamWriter(FileOutputStream(logLevel.file, true), "EUC-KR"))
                    try {
                        logLevel.waitingLogs.forEach { logMsg ->
                            bWriter.write("${logMsg}\r\n")
                        }
                        bWriter.flush()
                    } catch (ignored: IOException) {
                    } finally {
                        bWriter.close()
                    }
                }
            }
        }

        // Async / Sync 여부
        if (asAsync) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin) {
                taskRunnable.run()
            }
        } else {
            taskRunnable.run()
        }
    }


    // 로그
    fun log(logLevel: LogLevel, plugin: Plugin, vararg logs: String) {

        if (logLevel.howToStackLog == null) {
            logLevel.waitingLogs.add("")
            logLevel.waitingLogs.add("")
            logLevel.waitingLogs.add("[ ${plugin.name} ]")
            for (log in logs) {
                logLevel.waitingLogs.add("  $log")
            }
        }
        else {
            logLevel.howToStackLog.invoke(logLevel, plugin, logs.asList())
        }
    }
}
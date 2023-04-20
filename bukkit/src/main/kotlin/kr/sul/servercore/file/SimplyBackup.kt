package kr.sul.servercore.file

import kr.sul.servercore.ServerCore.Companion.plugin
import kr.sul.servercore.file.simplylog.LogLevel
import kr.sul.servercore.file.simplylog.SimplyLog
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import org.apache.commons.io.FileUtils
import org.bukkit.Bukkit
import java.io.File


// FIXME: 근데 Async로 하다가 쓰레드 2개가 동시에 같은 File을 사용하려 하면 어떡하지? 크래시 나지않나?
object SimplyBackup {
    /**
     * @param name null 시 자동으로 (whatToBackup 파일 or 폴더명)-(시간) 으로 저장
     * @param whatToBackupList 백업할 내용물 리스트
     * @param destinationFolder 백업 파일이 저장될 장소. 폴더가 없으면 생성
     * @param asZip compress as zip / only copy
     */
    // whatToBackUp 개수가 복수이며 asZip이 false일 경우, 폴더를 한 개 만들어서 거기에 whatToBackup 모조리 복사해 넣음
    fun backupFiles(name: String?, whatToBackupList: List<File>, destinationFolder: File, asZip: Boolean, asAsync: Boolean) {
        var nameWithoutExtension = name
        // 백업하는 코드
        val backupRunnable = Runnable {
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir()
            }

            // 이름 자동 생성
            if (nameWithoutExtension == null) {
                nameWithoutExtension = if (whatToBackupList.size > 1) {
                    "${whatToBackupList.first().nameWithoutExtension} 외 ${whatToBackupList.size-1}개 - ${CustomFileUtil.getFormattedDateTime()}"
                } else {
                    "${whatToBackupList.first().nameWithoutExtension} - ${CustomFileUtil.getFormattedDateTime()}"
                }
            }

            val destinationFile = run {  // 압축여부 구분
                if (asZip) {
                    File("${destinationFolder.path}/${nameWithoutExtension}.zip")
                } else {
                    if (whatToBackupList.size > 1) {
                        File("${destinationFolder.path}/${nameWithoutExtension}")
                    } else {
                        File("${destinationFolder.path}/${nameWithoutExtension}.${whatToBackupList.first().extension}")
                    }
                }
            }

            // destinationFile 이 이미 존재한다면, return
            if (destinationFile.exists()) {
                SimplyLog.log(LogLevel.ERROR_NORMAL, plugin,
                    "백업을 시도하려 했으나, destinationFile이 이미 존재하여 실패하였음.",
                    "destinationFile.path: ${destinationFile.path}")
                return@Runnable
            }

            // 압축해서 저장
            if (asZip) {
                val parameters = ZipParameters()
                parameters.compressionMethod = CompressionMethod.DEFLATE
                parameters.compressionLevel = CompressionLevel.NORMAL
                for (whatToBackup in whatToBackupList) {
                    if (whatToBackup.isFile) {
                        ZipFile("$destinationFile").addFile(whatToBackup, parameters)
                    } else if (whatToBackup.isDirectory) {
                        ZipFile("$destinationFile").addFolder(whatToBackup, parameters)
                    }
                }
            }
            // 오로지 카피만 해서 저장
            else {
                if (whatToBackupList.size > 1) {
                    // 폴더 생성 (여러개를 묶어주는 zip을 쓰지 않기 때문)
                    destinationFile.mkdir()
                    for (whatToBackup in whatToBackupList) {
                        if (whatToBackup.isDirectory) {
                            FileUtils.copyDirectory(whatToBackup, File("${destinationFile.toPath()}/${whatToBackup.name}"), false)
                        } else {
                            FileUtils.copyFile(whatToBackup, File("${destinationFile.toPath()}/${whatToBackup.name}"), false)
                        }
                    }
                } else {
                    if (whatToBackupList.first().isDirectory) {
                        FileUtils.copyDirectory(whatToBackupList.first(), destinationFile, false)
                    } else {
                        FileUtils.copyFile(whatToBackupList.first(), destinationFile, false)
                    }
                }
            }
        }


        // Async / Sync 여부
        if (asAsync) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin) { _ ->
                backupRunnable.run()
            }
        } else {
            backupRunnable.run()
        }
    }
    fun backupFile(name: String?, whatToBackup: File, destinationFolder: File, asZip: Boolean, asAsync: Boolean) {
        backupFiles(name, arrayListOf(whatToBackup), destinationFolder, asZip, asAsync)
    }
}
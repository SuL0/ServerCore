package kr.sul.servercore.file

import kr.sul.servercore.ServerCore.Companion.plugin
import kr.sul.servercore.file.simplylog.LogLevel
import kr.sul.servercore.file.simplylog.SimplyLog
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import org.bukkit.Bukkit
import java.io.File
import java.nio.file.Files


// FIXME: 근데 Async로 하다가 쓰레드 2개가 동시에 같은 File을 사용하려 하면 어떡하지? 크래시 나지않나?
object SimplyBackUp {
    /**
     * @param name null 시 자동으로 (whatToBackup 파일 or 폴더명)-(시간) 으로 저장
     * @param whatToBackUp 백업할 내용물
     * @param destinationFolder 백업 파일이 저장될 장소. 폴더가 없으면 생성
     * @param asZip compress as zip / only copy
     */
    fun backUpFile(name: String?, whatToBackUp: File, destinationFolder: File, asZip: Boolean, asAsync: Boolean) {
        var nameWithoutExtension = name
        // 백업하는 코드
        val backUpRunnable = Runnable {
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir()
            }

            // 이름 자동 생성
            if (nameWithoutExtension == null) {
                nameWithoutExtension = "${whatToBackUp.nameWithoutExtension} - ${CustomFileUtil.getFormattedDateTime()}"
            }



            val destinationFile = run {  // 압축여부 구분
                if (asZip) {
                    File("${destinationFolder.path}/${nameWithoutExtension}.zip")
                } else {
                    File("${destinationFolder.path}/${nameWithoutExtension}.${whatToBackUp.extension}")
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
                Files.copy(whatToBackUp.toPath(), destinationFile.toPath())
                /*
                // shade 문제인듯
                val parameters = ZipParameters()
                parameters.compressionMethod = CompressionMethod.STORE
                parameters.compressionLevel = CompressionLevel.NORMAL
                Bukkit.broadcastMessage("destinationFile: ${destinationFile.path}")
                ZipFile("$destinationFile").addFile(whatToBackUp, parameters)  // TODO: ZipFile 오류
                 */
            }
            // 오로지 카피만 해서 저장
            else {
                Files.copy(whatToBackUp.toPath(), destinationFile.toPath())
            }
        }


        // Async / Sync 여부
        if (asAsync) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin) {
                backUpRunnable.run()
            }
        } else {
            backUpRunnable.run()
        }
    }
}


//fun main() {
//    val parameters = ZipParameters()
//    parameters.compressionMethod = CompressionMethod.STORE
//    parameters.compressionLevel = CompressionLevel.NORMAL
//    val zip = ZipFile(File("E:/다운로드/백업테스트/asdf.zip"))
//    zip.addFile(File("C:/Users/PHR/Desktop/SERVER2/plugins/ItemFarming"), parameters)
//}
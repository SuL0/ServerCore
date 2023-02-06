package kr.sul.servercore.util

import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object Base64Serialization {
    // Bukkit Obj포함 Base64 Encoder/Decoder
    fun toBase64(any: Any): String {
        val outputStream = ByteArrayOutputStream()
        val dataOutput = BukkitObjectOutputStream(outputStream)
        dataOutput.writeObject(any)
        dataOutput.close()
        return Base64Coder.encodeLines(outputStream.toByteArray())
    }
    @Suppress("UNCHECKED_CAST")
    fun<T: Any> fromBase64(base64Str: String): T {
        val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(base64Str))
        val dataInput = BukkitObjectInputStream(inputStream)
        val returnVal = dataInput.readObject() as T
        dataInput.close()
        return returnVal
    }
}
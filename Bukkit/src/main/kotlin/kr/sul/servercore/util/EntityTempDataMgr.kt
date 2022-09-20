package kr.sul.servercore.util

import kr.sul.servercore.ServerCore.Companion.plugin
import kr.sul.servercore.file.simplylog.LogLevel
import kr.sul.servercore.file.simplylog.SimplyLog
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin


// EntityTempDataMap이 해당 코드 대체
// 이거 만들다가 어디선가 망해서 저걸로 넘어감
/*
/**
 * 플레이어는 나갈 시 데이터 소멸
 * 엔티티는 죽을 시 데이터 소멸
 * 서버가 종료되면 공통적으로 모두 데이터 소멸
 *
 * 플러그인별로 Storage 생성해서 각각 따로 사용함. 해당 플러그인 리로드되면 해당 플러그인에 해당하는 데이터 날려버림(미완?)
 * (한개의 플러그인이 리로드됐을 때 이전 CustomClass와 리로드된 CustomClass를 다르게 인식해서 발생하는 오류 때문)
 * (e.g. kr.sul.customClass cannot cast as kr.sul.customClass)
 */
object EntityTempDataMgr: Listener {
    private val dataStorage = hashMapOf<Plugin, HashMap<Entity, HashMap<String, Any>>>()  // <플러그인, <엔티티, <키, 값>>>

    fun<T: Any> Entity.setTempData(plugin: Plugin, key: String, data: T) {
        if (!dataStorage.containsKey(plugin)) {
            dataStorage[plugin] = hashMapOf()
        }
        if (!dataStorage[plugin]!!.containsKey(this)) {
            dataStorage[plugin]!![this] = hashMapOf()
        }
        dataStorage[plugin]!![this]!![key] = data
    }

    // 그냥 Generic 때문에 복잡해져서, 괜히 성능 손해만 보는 것 같아서 안 만드는게 나을 듯 (-> getTempData만 사용)
//    fun<T: Any> Entity.hasTempData(plugin: Plugin, key: String, clazz: Class<T>): Boolean {
//        if (!dataStorage.containsKey(this)) return false
//        val value = dataStorage[this]!![key] ?: return false
//    }

    fun<T: Any> Entity.getTempData(plugin: Plugin, key: String): T? {
        @Suppress("UNCHECKED_CAST")
        try {
            val value = dataStorage[plugin]?.get(this)?.get(key)
                ?: return null
            return value as T
        } catch (exception: Exception) {
            SimplyLog.log(LogLevel.ERROR_NORMAL, plugin, "${this.name}, ${this.type} 의 Key $key 를 가져오는 중 value를 cast하는 데 문제가 발생")
            SimplyLog.log(LogLevel.ERROR_NORMAL, plugin, exception.stackTraceToString())
            return null
        }
    }



    @EventHandler(priority = EventPriority.HIGH)
    fun onQuit(e: PlayerQuitEvent) {
        // 플러그인마다 루프
        if (dataStorage.contains(e.player)) {
            dataStorage.remove(e.player)
        }
    }

    // 플레이어를 제외한 엔티티는 죽을 시 영구히 존재가 소멸되기에 dataStorage 에서 데이터 삭제
    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityDeath(e: EntityDeathEvent) {
        if (e.isCancelled) return
        if (e.entity is Player) return
        // 플러그인마다 루프
        if (dataStorage.contains(e.entity)) {
            dataStorage.remove(e.entity)
        }
    }
}
*/
package kr.sul.servercore

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import redis.clients.jedis.JedisPool
import kotlin.system.measureTimeMillis

object JedisTest: Listener {
    var pool = lazy {
        val pool = JedisPool("localhost", 6379)
        val jedis = pool.resource
        jedis.hset("key1", "field1", "value1")
        testVar = pool.resource.hget("key1", "field1")
        pool
    }
    lateinit var testVar: String

    // DB에서 직접 값 변경
    @EventHandler
    fun onCmd(e: PlayerCommandPreprocessEvent) {
        if (!e.player.isOp) return
        if (e.message.startsWith("/jedis")) {
            // MySQL이랑 속도 비교
            e.isCancelled = true
            val cnt = e.message.split(" ")[1].toInt()
            val jedis = pool.value.resource
            val elapsed = measureTimeMillis {
                for (i in 1..cnt) {
                    jedis.hget("key1", "field1")
                }
            }
            Bukkit.broadcastMessage("elapsed : $elapsed")
        }
    }
}

fun main() {
    val jedis = JedisTest.pool.value.resource
    jedis.hset("key1", "field1", "value1")
    println(JedisTest.pool.value.resource.hget("key1", "field1"))
}
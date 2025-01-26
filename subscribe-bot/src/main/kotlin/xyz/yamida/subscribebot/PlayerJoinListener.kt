package xyz.yamida.subscribebot

import com.google.gson.JsonParser
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CompletableFuture

class PlayerJoinListener(val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val nickname = player.name

        CompletableFuture.runAsync {
            try {
                val daysLeft = getSubscriptionDaysLeft(nickname)
                Bukkit.getScheduler().runTask(
                    plugin, Runnable {
                    if (daysLeft > 0) {
                        val command = "lp user $nickname parent addtemp sub ${daysLeft}d"
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
                    } else {
                        val removeCommand = "lp user $nickname parent remove sub"
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), removeCommand)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSubscriptionDaysLeft(nickname: String): Int {
        val url = URL("http://89.39.121.106:8082/api/users/subscription/days-left?nickname=$nickname")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            val responseCode = connection.responseCode
            if (responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JsonParser.parseString(response).asJsonObject
                jsonObject.get("daysLeft").asInt
            } else {
                throw Exception("Invalid response code: $responseCode")
            }
        } finally {
            connection.disconnect()
        }
    }
}

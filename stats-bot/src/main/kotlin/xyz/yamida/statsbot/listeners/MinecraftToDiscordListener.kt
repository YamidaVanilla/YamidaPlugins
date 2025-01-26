package xyz.yamida.statsbot.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MinecraftToDiscordListener(val channel: TextChannel) : Listener {
    val plainTextSerializer = PlainTextComponentSerializer.plainText()
    val adPattern = Regex("""\b(?:\w+\.)?(?:gg|com|ru|net|org|xyz|tk|io|cf|ly)/[^\s]+\b""", RegexOption.IGNORE_CASE)
    val recentMessages = mutableMapOf<String, MutableList<String>>()

    @EventHandler
    fun onPlayerChat(event: AsyncChatEvent) {
        val playerName = event.player.name
        val message = plainTextSerializer.serialize(event.message())

        if (isAdvertisement(playerName, message)) {
            event.isCancelled = true
            channel.sendMessage("⚠️ **$playerName** попытался отправить подозрительное сообщение").queue()
            return
        }

        val sanitizedMessage = message.replace("@everyone", "Все", ignoreCase = true)
            .replace("@here", "Все", ignoreCase = true)

        channel.sendMessage("**$playerName**: $sanitizedMessage").queue()
    }

    fun isAdvertisement(playerName: String, message: String): Boolean {
        if (adPattern.containsMatchIn(message)) return true

        val messages = recentMessages.getOrPut(playerName) { mutableListOf() }
        if (messages.size >= 5 && messages.all { it == message }) {
            return true
        }

        messages.add(message)
        if (messages.size > 10) {
            messages.removeAt(0)
        }

        return false
    }
}
package dev.nobrain.statsbot.listeners

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.text.Component
import org.bukkit.Server

class DiscordToMinecraftListener(val server: Server) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return
        if (event.channel.id != "1328812726163607624") return

        val playerName = event.author.name
        val originalMessage = event.message
        val referencedMessage = originalMessage.referencedMessage
        val contentDisplay = originalMessage.contentDisplay
        val contentStripped = originalMessage.contentStripped

        val messageContent = contentDisplay.ifBlank { contentStripped }
        if (messageContent.isBlank()) {
            server.broadcast(Component.text("§9[Discord] §f$playerName: §7[Не удалось получить содержимое сообщения]"))
            return
        }

        if (referencedMessage != null) {
            val repliedUser = referencedMessage.author.name
            server.broadcast(
                Component.text("§9[Discord] §f$playerName ответил §f$repliedUser: §7$messageContent")
            )
        } else {
            server.broadcast(Component.text("§9[Discord] §f$playerName: §7$messageContent"))
        }
    }
}

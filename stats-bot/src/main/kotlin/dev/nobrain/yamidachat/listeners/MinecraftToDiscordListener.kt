package dev.nobrain.yamidachat.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MinecraftToDiscordListener(private val channel: TextChannel) : Listener {
    @EventHandler
    fun onPlayerChat(event: AsyncChatEvent) {
        val playerName = event.player.name
        val messageContent = PlainTextComponentSerializer.plainText().serialize(event.message())
        channel.sendMessage("**$playerName**: $messageContent").queue()
    }
}
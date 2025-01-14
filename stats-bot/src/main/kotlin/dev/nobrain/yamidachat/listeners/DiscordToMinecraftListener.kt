package dev.nobrain.yamidachat.listeners

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Server

class DiscordToMinecraftListener(private val server: Server) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return

        val channel = event.channel
        val message = event.message.contentDisplay
        val playerName = event.author.name

        server.broadcast(Component.text("§9[Discord] §f$playerName: §7$message"))
    }
}

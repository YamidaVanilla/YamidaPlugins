package dev.nobrain.statsbot.commands

import dev.nobrain.statsbot.api.SlashCommand
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.Server

class OnlineCommand(val server: Server) : SlashCommand() {
    override val name = "online"
    override val description = "Показывает количество онлайн игроков"

    override fun handle(event: SlashCommandInteractionEvent) {
        val onlinePlayers = server.onlinePlayers
        event.reply("Игроков онлайн: ${onlinePlayers.size}").setEphemeral(true).queue()
    }
}
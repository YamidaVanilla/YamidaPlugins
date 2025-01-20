package dev.nobrain.statsbot.commands

import dev.nobrain.statsbot.api.SlashCommand
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.Server

class PlayersCommand(val server: Server) : SlashCommand() {
    override val name = "players"
    override val description = "Список всех онлайн игроков"

    override fun handle(event: SlashCommandInteractionEvent) {
        val players = server.onlinePlayers
        val playerNames = players.joinToString { it.name }
        event.reply("Онлайн игроки (${players.size}): $playerNames").setEphemeral(true).queue()
    }
}
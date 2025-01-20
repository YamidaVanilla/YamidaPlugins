package dev.nobrain.statsbot.commands

import dev.nobrain.statsbot.api.SlashCommand
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.Server

class TpsCommand(val server: Server) : SlashCommand() {
    override val name = "tps"
    override val description = "Показывает текущий TPS сервера"

    override fun handle(event: SlashCommandInteractionEvent) {
        val tps = server.tps
        event.reply("TPS сервера: ${tps.joinToString { "%.2f".format(it) }}").setEphemeral(true).queue()
    }
}
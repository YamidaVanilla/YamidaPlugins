package xyz.yamida.statsbot.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.Server
import xyz.yamida.jda.commander.SlashCommand

class TpsCommand(val server: Server) : SlashCommand() {
    override val name = "tps"
    override val description = "Показывает текущий TPS сервера"

    override fun execute(event: SlashCommandInteractionEvent) {
        val tps = server.tps
        event.reply("TPS сервера: ${tps.joinToString { "%.2f".format(it) }}").setEphemeral(true).queue()
    }
}
package xyz.yamida.statsbot.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.Server
import xyz.yamida.jda.commander.SlashCommand

class OnlineCommand(val server: Server) : SlashCommand() {
    override val name = "online"
    override val description = "Показывает количество онлайн игроков"

    override fun execute(event: SlashCommandInteractionEvent) {
        val onlinePlayers = server.onlinePlayers
        event.reply("Игроков онлайн: ${onlinePlayers.size}").setEphemeral(true).queue()
    }
}
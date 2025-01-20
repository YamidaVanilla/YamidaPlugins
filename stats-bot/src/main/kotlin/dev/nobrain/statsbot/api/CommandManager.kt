package dev.nobrain.statsbot.api


import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands

class CommandManager : ListenerAdapter() {
    val guildCommands = mutableMapOf<String, MutableList<SlashCommand>>()

    fun registerCommand(vararg commands: SlashCommand) {
        commands.forEach { command ->
            guildCommands.computeIfAbsent("1291350039914086410") { mutableListOf() }.add(command)
            println("Registered command ${command.name}")
        }
    }

    override fun onGuildReady(event: GuildReadyEvent) {
        guildCommands[event.guild.id]?.let { commands ->
            val commandData = commands.map {
                Commands.slash(it.name, it.description).addOptions(it.options)
            }
            event.guild.updateCommands().addCommands(commandData).queue()
        }
        println("Commands registered for guild ${event.guild.name}")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command = guildCommands[event.guild?.id]?.find { it.name == event.name }

        if (command != null) {
            command.handle(event)
        } else {
            val embed = EmbedBuilder()
                .setTitle("Ошибка")
                .setDescription("Команда не найдена.")
                .setColor(0xFF0000)
                .build()

            event.replyEmbeds(embed)
                .setEphemeral(true)
                .queue()
        }
    }
}
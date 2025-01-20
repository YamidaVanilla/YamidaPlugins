package dev.nobrain.statsbot.commands

import dev.nobrain.statsbot.api.SlashCommand
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class MemoryCommand: SlashCommand() {
    override val name = "memory"
    override val description = "Показывает использование памяти сервера"

    override fun handle(event: SlashCommandInteractionEvent) {
        val runtime = Runtime.getRuntime()
        val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
        val maxMemory = runtime.maxMemory() / (1024 * 1024)
        event.reply("Использование памяти: ${usedMemory}MB / ${maxMemory}MB").setEphemeral(true).queue()
    }
}
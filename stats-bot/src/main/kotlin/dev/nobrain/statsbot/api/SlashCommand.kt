package dev.nobrain.statsbot.api

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData

abstract class SlashCommand {
    abstract val name: String
    abstract val description: String
    open val options: List<OptionData> = emptyList()
    open val requiredPermissions: List<Permission> = emptyList()

    abstract fun handle(event: SlashCommandInteractionEvent)
}

package xyz.yamida.statsbot

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.requests.GatewayIntent
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import xyz.yamida.jda.commander.CommandManager
import xyz.yamida.statsbot.commands.MemoryCommand
import xyz.yamida.statsbot.commands.OnlineCommand
import xyz.yamida.statsbot.commands.PlayersCommand
import xyz.yamida.statsbot.commands.TpsCommand
import xyz.yamida.statsbot.listeners.DiscordToMinecraftListener
import xyz.yamida.statsbot.listeners.MinecraftToDiscordListener
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class Main : JavaPlugin() {
    lateinit var bot: JDA
    val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    override fun onEnable() {
        saveDefaultConfig()
        logger.info("Enabling Discord bot...")

        val token = config.getString("discord.token")
            ?: return disableWithError("Discord token is missing!")

        val channelId = config.getString("discord.channel").takeUnless { it.isNullOrEmpty() }
            ?: return disableWithError("Discord channel ID is missing!")

        try {
            bot = JDABuilder.createLight(
                token,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MEMBERS
            ).build().awaitReady()
        } catch (e: Exception) {
            return disableWithError("Failed to initialize Discord bot: ${e.message}")
        }

        val voiceChannelId = "1332098490318327900"
        val voiceChannel = bot.getVoiceChannelById(voiceChannelId)
            ?: return disableWithError("No Discord voice channel found with ID: $voiceChannelId")

        startOnlineUpdater(voiceChannel)

        val textChannel = bot.getTextChannelById(channelId)
            ?: return disableWithError("No Discord text channel found with the given ID!")

        server.pluginManager.registerEvents(MinecraftToDiscordListener(textChannel), this)
        bot.addEventListener(DiscordToMinecraftListener(server))

        registerSlashCommands()
        logger.info("Discord bot started successfully.")
    }

    override fun onDisable() {
        if (::bot.isInitialized) {
            bot.shutdown()
        }
        scheduler.shutdownNow()
        logger.info("Discord bot shut down.")
    }

    fun startOnlineUpdater(voiceChannel: VoiceChannel) {
        scheduler.scheduleAtFixedRate({
            val onlinePlayers = Bukkit.getOnlinePlayers().size
            val newChannelName = "Онлайн ┃ $onlinePlayers"

            voiceChannel.manager.setName(newChannelName).queue(
                { logger.info("Updated voice channel name to: $newChannelName") },
                { logger.warning("Failed to update channel name: ${it.message}") }
            )
        }, 0, 5, TimeUnit.MINUTES)
    }

    fun disableWithError(message: String) {
        logger.severe(message)
        server.pluginManager.disablePlugin(this)
    }

    fun registerSlashCommands() {
        val commands = listOf(
            OnlineCommand(server),
            TpsCommand(server),
            MemoryCommand(),
            PlayersCommand(server)
        )

        val commandManager = CommandManager(commands)
        bot.addEventListener(commandManager)
    }
}
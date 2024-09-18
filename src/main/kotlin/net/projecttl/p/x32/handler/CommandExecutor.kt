package net.projecttl.p.x32.handler

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

interface CommandExecutor {
	val data: CommandData
}

interface GlobalCommand : CommandExecutor {
	override val data: CommandData
	suspend fun execute(ev: SlashCommandInteractionEvent)
}

interface UserContext : CommandExecutor {
	override val data: CommandData
	suspend fun execute(ev: UserContextInteractionEvent)
}

interface MessageContext : CommandExecutor {
	override val data: CommandData
	suspend fun execute(ev: MessageContextInteractionEvent)
}

package net.wh64.p.x32

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

interface SlashCommandExecutor {
	val data: CommandData
	fun execute(ev: SlashCommandInteractionEvent)
}

interface UserContextExecutor {
	val data: CommandData
	fun execute(ev: UserContextInteractionEvent)
}

interface MessageContextExecutor {
	val data: CommandData
	fun execute(ev: MessageContextInteractionEvent)
}

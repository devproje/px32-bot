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

abstract class CommandAdapter : SlashCommandExecutor, UserContextExecutor, MessageContextExecutor {
	abstract override val data: CommandData

	override fun execute(ev: SlashCommandInteractionEvent) {
		executor(ev)
	}

	override fun execute(ev: UserContextInteractionEvent) {
		executor(ev)
	}

	override fun execute(ev: MessageContextInteractionEvent) {
		executor(ev)
	}

	abstract fun executor(ev: Any)
}

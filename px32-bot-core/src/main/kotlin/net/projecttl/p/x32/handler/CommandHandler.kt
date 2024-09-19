package net.projecttl.p.x32.handler

import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.projecttl.p.x32.logger

class CommandHandler(val guildId: Long = 0L) : ListenerAdapter() {
	private val commands = mutableListOf<CommandExecutor>()

	override fun onSlashCommandInteraction(ev: SlashCommandInteractionEvent) {
		val name = ev.interaction.name

		commands.forEach { command ->
			if (command.data.name == name) {
				if (command is GlobalCommand) {
					runBlocking {
						command.execute(ev)
					}

					return@forEach
				}
			}
		}
	}

	override fun onUserContextInteraction(ev: UserContextInteractionEvent) {
		val name = ev.interaction.name

		commands.forEach { command ->
			if (command.data.name == name) {
				if (command is UserContext) {
					runBlocking {
						command.execute(ev)
					}

					return@forEach
				}
			}
		}
	}

	override fun onMessageContextInteraction(ev: MessageContextInteractionEvent) {
		val name = ev.interaction.name

		commands.forEach { command ->
			if (command.data.name == name) {
				if (command is MessageContext) {
					runBlocking {
						command.execute(ev)
					}

					return@forEach
				}
			}
		}
	}

	fun addCommand(command: CommandExecutor) {
		commands.add(command)
	}

	fun delCommand(command: CommandExecutor) {
		commands.remove(command)
	}

	fun register(jda: JDA) {
		val guild = jda.getGuildById(guildId)
		if (guildId != 0L) {
			if (guild == null) {
				logger.info("'${guildId}' guild is not exists!")
				return
			}
		}

		commands.forEach { command ->
			val data = command.data

			if (command is GlobalCommand) {
				if (guild == null) {
					jda.upsertCommand(data).queue()
					logger.info("Register Global Command: /${data.name}")
				} else {
					guild.upsertCommand(data).queue()
					logger.info("Register '${guild.id}' Guild's Command: /${data.name}")
				}
			}

			if (command is UserContext) {
				if (guild == null) {
					jda.updateCommands().addCommands(
						Commands.context(Command.Type.USER, data.name),
						Commands.message(data.name)
					).queue()

					logger.info("Register User Context Command: /${data.name}")
				} else {
					guild.updateCommands().addCommands(
						Commands.context(Command.Type.USER, data.name),
						Commands.message(data.name)
					).queue()
					logger.info("Register '${guild.id}' Guild's User Context Command: /${data.name}")
				}
			}

			if (command is MessageContext) {
				if (guild == null) {
					jda.updateCommands().addCommands(
						Commands.context(Command.Type.MESSAGE, data.name),
						Commands.message(data.name)
					)

					logger.info("Register Message Context Command: /${data.name}")
				} else {
					guild.updateCommands().addCommands(
						Commands.context(Command.Type.MESSAGE, data.name),
						Commands.message(data.name)
					)

					logger.info("Register '${guild.id}' Guild's Message Context Command: /${data.name}")
				}
			}
		}
	}
}
package net.projecttl.p.x32.api.command

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands

fun commandHandler(guildId: Long = 0L, block: (CommandHandler) -> Unit): CommandHandler {
	val handler = CommandHandler(guildId)
	block.invoke(handler)

	return handler
}

@OptIn(DelicateCoroutinesApi::class)
class CommandHandler(val guildId: Long = 0L) : ListenerAdapter() {
	private val commands = mutableListOf<CommandExecutor>()

	override fun onSlashCommandInteraction(ev: SlashCommandInteractionEvent) {
		val name = ev.interaction.name

		commands.singleOrNull { it.data.name == name }?.also { command ->
			if (command is GlobalCommand) {
				GlobalScope.launch {
					command.execute(ev)
					println("${ev.user.id} is use command for: ${ev.interaction.name}")
				}

				return
			}
		}
	}

	override fun onUserContextInteraction(ev: UserContextInteractionEvent) {
		val name = ev.interaction.name

		commands.singleOrNull { it.data.name == name }?.also { command ->
			if (command is UserContext) {
				GlobalScope.launch {
					command.execute(ev)
					println("${ev.user.id} is use user context for: ${ev.interaction.name}")
				}

				return
			}
		}
	}

	override fun onMessageContextInteraction(ev: MessageContextInteractionEvent) {
		val name = ev.interaction.name

		commands.singleOrNull { it.data.name == name }?.also { command ->
			if (command is MessageContext) {
				GlobalScope.launch {
					command.execute(ev)
					println("${ev.user.id} is use message context for: ${ev.interaction.name}")
				}

				return
			}
		}
	}

	fun addCommand(command: CommandExecutor) {
		commands.add(command)
	}

	fun delCommand(command: CommandExecutor) {
		commands.remove(command)
	}

	fun getCommands(): List<CommandExecutor> {
		return commands
	}

	fun register(jda: JDA) {
		val guild = jda.getGuildById(guildId)
		if (guildId != 0L) {
			if (guild == null) {
				println("'${guildId}' guild is not exists!")
				return
			}
		}

		commands.forEach { command ->
			val data = command.data

			if (command is GlobalCommand) {
				if (guild == null) {
					jda.upsertCommand(data).queue()
					println("Register Global Command: /${data.name}")
				} else {
					guild.upsertCommand(data).queue()
					println("Register '${guild.id}' Guild's Command: /${data.name}")
				}
			}

			if (command is UserContext) {
				if (guild == null) {
					jda.upsertCommand(Commands.user(data.name)).queue()

					println("Register User Context Command: /${data.name}")
				} else {
					guild.upsertCommand(Commands.user(data.name)).queue()
					println("Register '${guild.id}' Guild's User Context Command: /${data.name}")
				}
			}

			if (command is MessageContext) {
				if (guild == null) {
					jda.upsertCommand(Commands.message(data.name)).queue()
					println("Register Message Context Command: /${data.name}")
				} else {
					guild.upsertCommand(Commands.message(data.name)).queue()
					println("Register '${guild.id}' Guild's Message Context Command: /${data.name}")
				}
			}
		}
	}
}
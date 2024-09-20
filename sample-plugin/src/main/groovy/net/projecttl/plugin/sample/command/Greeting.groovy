package net.projecttl.plugin.sample.command

import kotlin.Unit
import kotlin.coroutines.Continuation
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import net.projecttl.p.x32.api.command.GlobalCommand

class Greeting implements GlobalCommand {
	@Override
	CommandData getData() {
		return CommandData.fromData(
			new CommandDataImpl("greeting", "greeting for user!").toData()
		)
	}

	@Override
	Object execute(SlashCommandInteractionEvent ev, Continuation<? super Unit> $completion) {
		ev.reply("Hello, ${ev.user.globalName == null ? ev.user.name : ev.user.globalName}").queue()
		return null
	}
}

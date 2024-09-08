package net.wh64.p.x32

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class GlobalCommandHandler : ListenerAdapter() {
	override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
	}

	override fun onUserContextInteraction(event: UserContextInteractionEvent) {
	}

	override fun onMessageContextInteraction(event: MessageContextInteractionEvent) {
	}
}
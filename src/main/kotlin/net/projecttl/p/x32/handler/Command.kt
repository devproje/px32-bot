package net.projecttl.p.x32.handler

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

interface Command {
	val data: CommandData
	fun execute(ev: SlashCommandInteractionEvent)
}
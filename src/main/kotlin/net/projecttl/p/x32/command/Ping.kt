package net.projecttl.p.x32.command

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import net.projecttl.p.x32.handler.GlobalCommand
import net.projecttl.p.x32.util.colour
import kotlin.random.Random

object Ping : GlobalCommand {
	override val data: CommandData = CommandData.fromData(CommandDataImpl(
		"ping",
		"Discord API 레이턴시 확인 합니다"
	).toData())

	override suspend fun execute(ev: SlashCommandInteractionEvent) {
		val embed = measure(ev.jda)
		ev.replyEmbeds(embed).queue()
	}

	private fun measure(jda: JDA): MessageEmbed {
		val embed = EmbedBuilder()
		embed.setTitle(":ping_pong: **Pong!**")
		embed.addField(":electric_plug: **API**", "`${jda.gatewayPing}ms`", true)
		embed.colour()

		return embed.build()
	}
}
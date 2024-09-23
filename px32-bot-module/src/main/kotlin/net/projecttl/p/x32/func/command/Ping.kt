package net.projecttl.p.x32.func.command

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import net.projecttl.p.x32.api.command.GlobalCommand
import net.projecttl.p.x32.api.util.colour
import net.projecttl.p.x32.api.util.footer

object Ping : GlobalCommand {
	override val data: CommandData = CommandData.fromData(CommandDataImpl(
		"ping",
		"Discord API 레이턴시 확인 합니다"
	).toData())

	override suspend fun execute(ev: SlashCommandInteractionEvent) {
		val started = System.currentTimeMillis()
		var embed = EmbedBuilder().let {
			it.setTitle(":hourglass: Just wait a sec...")
			it.colour()

			it
		}.build()

		ev.replyEmbeds(embed).queue {
			embed = measure(started, ev.user, ev.jda)
			it.editOriginalEmbeds(embed).queue()
		}
	}

	private fun measure(started: Long, user: User, jda: JDA): MessageEmbed {
		val embed = EmbedBuilder()
		embed.setTitle(":ping_pong: **Pong!**")
		embed.addField(":electric_plug: **API**", "`${jda.gatewayPing}ms`", true)
		embed.addField(":robot: **BOT**", "`${System.currentTimeMillis() - started}ms`", true)
		embed.colour()
		embed.footer(user)

		return embed.build()
	}
}
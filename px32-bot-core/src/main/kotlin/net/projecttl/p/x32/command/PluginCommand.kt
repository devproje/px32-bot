package net.projecttl.p.x32.command

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.projecttl.p.x32.api.command.GlobalCommand
import net.projecttl.p.x32.api.command.useCommand
import net.projecttl.p.x32.api.util.colour
import net.projecttl.p.x32.api.util.footer
import net.projecttl.p.x32.kernel

object PluginCommand : GlobalCommand {
	override val data = useCommand {
		name = "plugins"
		description = "봇에 불러온 플러그인을 확인 합니다"
	}

	override suspend fun execute(ev: SlashCommandInteractionEvent) {
		val embed = EmbedBuilder().apply {
			setTitle(":sparkles: **${ev.jda.selfUser.name}**봇 플러그인 리스트")
			setThumbnail(ev.jda.selfUser.avatarUrl)

			colour()
			footer(ev.user)
		}

		val loader = kernel.plugins
		val fields = loader.map { (c, _) ->
			MessageEmbed.Field(":electric_plug: **${c.name}**", "`${c.version}`", true)
		}

		fields.forEach { field ->
			embed.addField(field)
		}

		ev.replyEmbeds(embed.build()).queue()
	}
}
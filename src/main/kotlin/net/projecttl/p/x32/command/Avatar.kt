package net.projecttl.p.x32.command

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import net.projecttl.p.x32.handler.UserContext
import net.projecttl.p.x32.util.colour

object Avatar : UserContext {
	override val data = CommandData.fromData(CommandDataImpl("avatar", "유저의 프로필 이미지를 가져 옵니다").toData())

	override suspend fun execute(ev: UserContextInteractionEvent) {
		val embed = EmbedBuilder()
		embed.setTitle(":frame_photo: ${ev.name} Avatar")
		embed.setImage(ev.target.effectiveAvatarUrl)
		embed.colour()

		ev.replyEmbeds(embed.build()).queue()
	}
}
package net.projecttl.p.x32.func.command

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.projecttl.p.x32.api.command.UserContext
import net.projecttl.p.x32.api.command.useCommand
import net.projecttl.p.x32.api.util.colour
import net.projecttl.p.x32.api.util.footer

object Avatar : UserContext {
	override val data = useCommand {
		name = "avatar"
		description = "유저의 프로필 이미지를 가져 옵니다"
	}

	override suspend fun execute(ev: UserContextInteractionEvent) {
		val embed = EmbedBuilder()
		embed.setTitle(":frame_photo: ${ev.target.name}'s Avatar")
		embed.setImage("${ev.target.effectiveAvatarUrl}?size=512")
		embed.colour()
		embed.footer(ev.user)

		ev.replyEmbeds(embed.build()).queue()
	}
}

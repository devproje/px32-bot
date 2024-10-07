package net.projecttl.p.x32.func.command

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.projecttl.p.x32.api.command.MessageContext
import net.projecttl.p.x32.api.command.useCommand

object MsgLength : MessageContext {
	override val data = useCommand {
		name = "length"
		description = "메시지의 길이를 확인 합니다."
	}

	override suspend fun execute(ev: MessageContextInteractionEvent) {
		val target = ev.target
		ev.reply("${target.jumpUrl} 메시지의 길이: ${target.contentRaw.length}").queue()
	}
}

package net.projecttl.p.x32.func.command

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import net.projecttl.p.x32.api.command.MessageContext

object MsgLength : MessageContext {
	override val data = CommandData.fromData(CommandDataImpl("length", "메시지의 길이를 확인 합니다.").toData())

	override suspend fun execute(ev: MessageContextInteractionEvent) {
		val target = ev.target
		ev.reply("${target.jumpUrl} 메시지의 길이: ${target.contentRaw.length}").queue()
	}
}

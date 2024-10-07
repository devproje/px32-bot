package net.projecttl.p.x32.func.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.projecttl.p.x32.api.command.GlobalCommand
import net.projecttl.p.x32.api.command.useCommand
import net.projecttl.p.x32.func.Conf

object MsgPurge : GlobalCommand {
	override val data = useCommand {
		name = "purge"
		description = "n개의 메시지를 채널에서 삭제해요"
		option {
			type = OptionType.INTEGER
			name = "n"
			description = "n개만큼 메시지가 삭제 됩니다"
			required = true
		}
	}

	override suspend fun execute(ev: SlashCommandInteractionEvent) {
		val n = ev.getOption("n")!!.asInt
		if (n !in 1..100) {
			return ev.reply(":warning: 1 부터 100까지 가능해요").setEphemeral(true).queue()
		}

		if (ev.user.id != Conf.owner) {
			ev.reply(":warning: 이 명령어는 봇 관리자만 사용 가능해요").setEphemeral(true).queue()
			return
		}

		try {
			ev.channel.iterableHistory.takeAsync(n).thenAccept(ev.channel::purgeMessages)
		} catch (ex: Exception) {
			ex.printStackTrace()
			return ev.reply(":warning: 메시지 삭제 도중에 문제가 발생 했어요").queue()
		}

		ev.reply(":white_check_mark: `${n}`개의 메시지를 삭제 했어요").queue()
	}
}
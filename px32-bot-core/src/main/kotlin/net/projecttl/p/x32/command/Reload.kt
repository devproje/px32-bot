package net.projecttl.p.x32.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import net.projecttl.p.x32.api.command.GlobalCommand
import net.projecttl.p.x32.config.Config
import net.projecttl.p.x32.kernel

object Reload : GlobalCommand {
	override val data = CommandData.fromData(CommandDataImpl("reload", "플러그인을 다시 불러 옵니다").toData())

	override suspend fun execute(ev: SlashCommandInteractionEvent) {
		if (kernel.memLock.isLocked) {
			return
		}

		if (ev.user.id != Config.owner) {
			return ev.reply(":warning: 권한을 가지고 있지 않아요").queue()
		}

		try {
			kernel.reload(ev.jda)
		} catch (ex: Exception) {
			ex.printStackTrace()
			ev.reply(":warning: 플러그인을 다시 불러오는 도중에 오류가 발생 했어요. 자세한 내용은 콘솔을 확인해 주세요!").queue()
			return
		}

		ev.reply(":white_check_mark: 플러그인을 다시 불러 왔어요!\n불러온 플러그인 수: ${kernel.plugins.size}").queue()
	}
}
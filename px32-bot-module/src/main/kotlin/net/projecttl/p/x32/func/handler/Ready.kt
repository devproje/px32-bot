package net.projecttl.p.x32.func.handler

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.projecttl.p.x32.func.Conf
import net.projecttl.p.x32.func.instance

object Ready : ListenerAdapter() {
	@OptIn(DelicateCoroutinesApi::class)
	override fun onReady(ev: ReadyEvent) {
		val list = listOf(
			Activity.playing("${ev.jda.selfUser.name} v${Conf.version}"),
			Activity.listening("${ev.guildTotalCount}개의 서버와 함께 서비스 하는 중")
		)
		println("Logged in as ${ev.jda.selfUser.asTag}")

		GlobalScope.launch {
			do {
				list.forEach { act ->
					try {
						ev.jda.presence.setPresence(OnlineStatus.ONLINE, act)
					} catch (ex: Exception) {
						instance.logger.error(ex.message)
					}
					delay(1000 * 10L)
				}
			} while (true)
		}
	}
}
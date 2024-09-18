package net.projecttl.p.x32.handler

import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.projecttl.p.x32.logger

object Ready : ListenerAdapter() {
	override fun onReady(ev: ReadyEvent) {
		logger.info("Logged in as ${ev.jda.selfUser.asTag}")
	}
}
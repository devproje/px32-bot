package net.projecttl.p.x32.func.handler

import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object Ready : ListenerAdapter() {
	override fun onReady(ev: ReadyEvent) {
		println("Logged in as ${ev.jda.selfUser.asTag}")
	}
}
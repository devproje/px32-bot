package net.projecttl.p.x32.kernel

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.projecttl.p.x32.handler.CommandHandler

class CoreKernel(token: String) {
	private val builder = JDABuilder.createDefault(token)
	private val handlers = mutableListOf<ListenerAdapter>()

	private val handler = CommandHandler()

	fun getGlobalCommandHandler(): CommandHandler {
		return handler
	}

	fun addHandler(handler: ListenerAdapter) {
		handlers.add(handler)
	}

	fun delHandler(handler: ListenerAdapter) {
		handlers.remove(handler)
	}

	fun build(): JDA {
		handlers.map {
			builder.addEventListeners(it)
		}
		builder.addEventListeners(handler)

		val jda = builder.build()
		handler.register(jda)
		handlers.forEach { h ->
			if (h is CommandHandler) {
				h.register(jda)
			}
		}

		return jda
	}
}
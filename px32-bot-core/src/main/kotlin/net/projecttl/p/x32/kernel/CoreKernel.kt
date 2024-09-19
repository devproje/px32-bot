package net.projecttl.p.x32.kernel

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.projecttl.p.x32.api.command.CommandHandler
import net.projecttl.p.x32.logger

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
		PluginLoader.load()

		val plugins = PluginLoader.getPlugins()
		plugins.forEach { (c, p) ->
			logger.info("Load plugin ${c.name} v${c.version}")
			p.onLoad()

			p.getHandlers().map { handler ->
				builder.addEventListeners(handler)
			}
		}

		val jda = builder.build()
		handler.register(jda)
		handlers.forEach { h ->
			if (h is CommandHandler) {
				h.register(jda)
			}
		}

		Runtime.getRuntime().addShutdownHook(Thread {
			PluginLoader.destroy()
		})

		return jda
	}
}
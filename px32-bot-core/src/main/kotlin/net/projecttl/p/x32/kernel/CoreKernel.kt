package net.projecttl.p.x32.kernel

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.projecttl.p.x32.api.Plugin
import net.projecttl.p.x32.api.command.CommandHandler
import net.projecttl.p.x32.jda
import net.projecttl.p.x32.logger

class CoreKernel(token: String) {
	private val builder = JDABuilder.createDefault(token)
	private val handlers = mutableListOf<ListenerAdapter>()
	private val commandContainer = CommandHandler()

	fun getCommandContainer(): CommandHandler {
		return commandContainer
	}

	fun addHandler(handler: ListenerAdapter) {
		handlers.add(handler)
	}

	fun delHandler(handler: ListenerAdapter) {
		handlers.remove(handler)
	}

	fun plugins(): List<Plugin> {
		return PluginLoader.getPlugins().map { it.value }
	}

	fun build(): JDA {
		PluginLoader.load()

		plugins().forEach { plugin ->
			plugin.getHandlers().forEach { handler ->
				handlers.add(handler)
			}
		}

		handlers.map {
			builder.addEventListeners(it)
		}
		builder.addEventListeners(commandContainer)

		val jda = builder.build()
		commandContainer.register(jda)
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

	fun reload() {
		val newHandlers = mutableListOf<ListenerAdapter>()
		PluginLoader.destroy()
		plugins().forEach { plugin ->
			plugin.getHandlers().forEach { handler ->
				if (handlers.contains(handler)) {
					jda.removeEventListener(handler)
					handlers.remove(handler)
				}
			}
		}

		PluginLoader.load()

		plugins().forEach { plugin ->
			plugin.getHandlers().forEach { handler ->
				if (!handlers.contains(handler)) {
					handlers.add(handler)
					newHandlers.add(handler)
				}
			}
		}

		handlers.map {
			builder.addEventListeners(it)
		}

		newHandlers.forEach { h ->
			if (h is CommandHandler) {
				h.register(jda)
			}
		}
	}
}

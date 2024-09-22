package net.projecttl.p.x32.kernel

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.projecttl.p.x32.api.Plugin
import net.projecttl.p.x32.api.command.CommandHandler
import net.projecttl.p.x32.config.Config
import net.projecttl.p.x32.func.BundleModule
import net.projecttl.p.x32.jda
import net.projecttl.p.x32.logger

class CoreKernel(token: String) {
	var memLock = false
		private set
	private val builder = JDABuilder.createDefault(token, listOf(
		GatewayIntent.GUILD_PRESENCES,
		GatewayIntent.GUILD_MEMBERS,
		GatewayIntent.MESSAGE_CONTENT,
		GatewayIntent.GUILD_VOICE_STATES,
		GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
		GatewayIntent.SCHEDULED_EVENTS
	))
	private val handlers = mutableListOf<ListenerAdapter>()
	private val commandContainer = CommandHandler()

	private fun include() {
		if (Config.bundle) {
			val b = BundleModule()
			PluginLoader.putModule(b.config, b)
		}
	}

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
		include()

		PluginLoader.load()
		plugins().forEach { plugin ->
			plugin.handlers.forEach { handler ->
				handlers.add(handler)
			}
		}

		handlers.map {
			logger.info("Load event listener: ${it::class.simpleName}")
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
		if (!memLock) {
			memLock = true
		}

		val newHandlers = mutableListOf<ListenerAdapter>()
		PluginLoader.destroy()
		plugins().forEach { plugin ->
			plugin.handlers.forEach { handler ->
				if (handlers.contains(handler)) {
					jda.removeEventListener(handler)
					handlers.remove(handler)
				}
			}
		}

		include()
		PluginLoader.load()

		plugins().forEach { plugin ->
			plugin.handlers.forEach { handler ->
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

		memLock = false
	}
}

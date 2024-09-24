package net.projecttl.p.x32.kernel

import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.projecttl.p.x32.api.Plugin
import net.projecttl.p.x32.api.command.CommandHandler
import net.projecttl.p.x32.api.model.PluginConfig
import net.projecttl.p.x32.api.util.AsyncTaskContainer
import net.projecttl.p.x32.config.Config
import net.projecttl.p.x32.func.BundleModule
import net.projecttl.p.x32.logger
import java.io.File
import java.net.URLClassLoader
import java.nio.charset.Charset
import java.util.jar.JarFile

class CoreKernel(token: String) {
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

	val memLock = Mutex()
	val plugins get() = PluginLoader.getPlugins().map { it.value }

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

	fun build(): JDA {
		include()

		PluginLoader.load()
		plugins.forEach { plugin ->
			plugin.handlers.forEach { handler ->
				handlers.add(handler)
			}
		}

		handlers.map {
			logger.info("Load event listener: ${it::class.simpleName}")
			builder.addEventListeners(it)
		}
		builder.addEventListeners(commandContainer)

		return builder.build()
	}

	fun register(jda: JDA) {
		commandContainer.register(jda)
		handlers.forEach { h ->
			if (h is CommandHandler) {
				h.register(jda)
			}
		}

		Runtime.getRuntime().addShutdownHook(Thread {
			PluginLoader.destroy()
		})
	}

	suspend fun reload(jda: JDA) {
		if (!memLock.isLocked) {
			memLock.lock()
		}

		val newHandlers = mutableListOf<ListenerAdapter>()
		PluginLoader.destroy()
		plugins.forEach { plugin ->
			plugin.handlers.forEach { handler ->
				if (handlers.contains(handler)) {
					jda.removeEventListener(handler)
					handlers.remove(handler)
				}
			}
		}

		include()
		PluginLoader.load()

		plugins.forEach { plugin ->
			plugin.handlers.forEach { handler ->
				if (!handlers.contains(handler)) {
					handlers.add(handler)
					newHandlers.add(handler)
				}
			}
		}

		handlers.map {
			jda.addEventListener(it)
		}

		handlers.forEach { h ->
			if (h is CommandHandler) {
				h.register(jda)
			}
		}

		memLock.unlock()
	}

	object PluginLoader {
		private val plugins = mutableMapOf<PluginConfig, Plugin>()
		private val parentDir = File("./plugins").apply {
			if (!exists()) {
				mkdirs()
			}
		}

		fun getPlugins(): Map<PluginConfig, Plugin> {
			return plugins.toMap()
		}

		fun putModule(config: PluginConfig, plugin: Plugin) {
			try {
				logger.info("Load module ${config.name} v${config.version}")
				plugin.onLoad()
			} catch (ex: Exception) {
				ex.printStackTrace()
				plugin.destroy()
				return
			}

			plugins[config] = plugin
		}

		fun load() {
			parentDir.listFiles()?.forEach { file ->
				loadPlugin(file)
			}

			logger.info("Loaded ${plugins.size} plugins")
		}

		fun destroy() {
			plugins.forEach { (config, plugin) ->
				logger.info("disable ${config.name} plugin...")

				try {
					plugin.destroy()
				} catch (ex: Exception) {
					logger.error("failed to destroy ${config.name} plugin")
					ex.printStackTrace()
				}
			}
		}

		private fun loadPlugin(file: File) {
			if (file.name == "px32-bot-module") {
				return
			}

			if (!file.name.endsWith(".jar")) {
				return
			}

			val jar = JarFile(file)
			val cnf = jar.entries().toList().singleOrNull { jarEntry -> jarEntry.name == "plugin.json" }
			if (cnf == null)
				return logger.error("${file.name} is not a plugin. aborted")

			val stream = jar.getInputStream(cnf)
			val raw = stream.use {
				return@use it.readBytes().toString(Charset.forName("UTF-8"))
			}

			val config = Json.decodeFromString<PluginConfig>(raw)
			val cl = URLClassLoader(arrayOf(file.toPath().toUri().toURL()))
			val obj = cl.loadClass(config.main).getDeclaredConstructor().newInstance()

			if (obj !is Plugin)
				return logger.error("${config.name} is not valid main class. aborted")

			try {
				logger.info("Load plugin ${config.name} v${config.version}")
				obj.onLoad()
			} catch (ex: Exception) {
				ex.printStackTrace()
				return logger.error("Failed to load plugin ${config.name}")
			}

			plugins[config] = obj
		}
	}
}

package net.projecttl.p.x32.kernel

import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.projecttl.p.x32.api.BotConfig
import net.projecttl.p.x32.api.Plugin
import net.projecttl.p.x32.api.command.CommandHandler
import net.projecttl.p.x32.api.model.PluginConfig
import net.projecttl.p.x32.config.DefaultConfig
import net.projecttl.p.x32.func.BundleModule
import net.projecttl.p.x32.logger
import java.io.File
import java.net.URLClassLoader
import java.nio.charset.Charset
import java.util.jar.JarFile

class CoreKernel(token: String) {
	lateinit var jda: JDA
		private set

	private val builder = JDABuilder.createDefault(token, listOf(
		GatewayIntent.GUILD_MEMBERS,
		GatewayIntent.GUILD_MESSAGES,
		GatewayIntent.MESSAGE_CONTENT,
		GatewayIntent.GUILD_PRESENCES,
		GatewayIntent.SCHEDULED_EVENTS,
		GatewayIntent.GUILD_VOICE_STATES,
		GatewayIntent.GUILD_EMOJIS_AND_STICKERS
	)).setMemberCachePolicy(MemberCachePolicy.ALL)

	val memLock = Mutex()
	val commandContainer = CommandHandler()
	var plugins = mutableMapOf<PluginConfig, Plugin>()
		private set
	var isActive = false
		private set

	private val parentDir = File("./plugins").apply {
		if (!exists()) {
			mkdirs()
		}
	}

	val handlers: List<ListenerAdapter>
		get() {
			if (!isActive) {
				return listOf()
			}

			return jda.eventManager.registeredListeners.map { it as ListenerAdapter }
		}

	fun addHandler(handler: ListenerAdapter) {
		if (isActive) {
			jda.addEventListener(handler)
			return
		}

		builder.addEventListeners(handler)
	}

	fun delHandler(handler: ListenerAdapter) {
		if (isActive) {
			jda.removeEventListener(handler)
			return
		}

		builder.removeEventListeners(handler)
	}

	fun register(jda: JDA) {
		commandContainer.register(jda)
		jda.eventManager.registeredListeners.filterIsInstance<CommandHandler>().forEach { h ->
			h.register(jda)
		}

		Runtime.getRuntime().addShutdownHook(Thread {
			destroy()
		})
	}

	suspend fun reload(jda: JDA) {
		if (!memLock.isLocked) {
			memLock.lock()
		}

		destroy()
		load()
		handlers.filterIsInstance<CommandHandler>().forEach { h ->
			h.register(jda)
		}

		memLock.unlock()
	}

	fun kill() {
		destroy()
		jda.shutdownNow()
	}

	private fun load() {
		if (BotConfig.bundle) {
			val b = BundleModule()
			loadModule(b.config, b)
		}

		parentDir.listFiles()?.forEach { file ->
			try {
				loadPlugin(file)
			} catch (ex: Exception) {
				logger.error("error occurred while to plugin loading: ${ex.message}")
			}
		}

		var cnt = 0
		plugins.forEach { (_, plugin) ->
			plugin.handlers.forEach {
				addHandler(it)
				logger.info("Load event listener: ${it::class.simpleName}")
				cnt++
			}
		}

		logger.info("Loaded ${plugins.size} plugin${if (plugins.size > 1) "s" else ""} and $cnt handler${if (cnt > 1) "s" else ""}.")
	}

	private fun destroy() {
		plugins.forEach { (config, plugin) ->
			logger.info("disable ${config.name} plugin...")

			plugin.handlers.forEach {
				delHandler(it)
			}

			try {
				plugin.destroy()
			} catch (ex: Exception) {
				logger.error("failed to destroy ${config.name} plugin")
				ex.printStackTrace()
			}
		}

		plugins = mutableMapOf()
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
			throw IllegalAccessException("${file.name} is not a plugin. aborted")

		val stream = jar.getInputStream(cnf)
		val raw = stream.use {
			return@use it.readBytes().toString(Charset.forName("UTF-8"))
		}

		val config = Json.decodeFromString<PluginConfig>(raw)
		val cl = URLClassLoader(arrayOf(file.toPath().toUri().toURL()))
		val obj = cl.loadClass(config.main).getDeclaredConstructor().newInstance()

		if (obj !is Plugin)
			throw IllegalAccessException("${config.name} is not valid plugin class. aborted")

		try {
			loadModule(config, obj)
		} catch (ex: Exception) {
			throw ex
		}
	}

	private fun loadModule(config: PluginConfig, plugin: Plugin) {
		try {
			logger.info("Load plugin ${config.name} v${config.version}")
			plugin.onLoad()
		} catch (ex: Exception) {
			try {
				plugin.destroy()
			} catch (ex: Exception) {
				throw ex
			}

			throw ex
		}

		plugins[config] = plugin
	}

	fun build(): JDA {
		if (isActive) {
			logger.error("core kernel is already loaded! you cannot rebuild this kernel.")
			return jda
		}

		load()
		builder.addEventListeners(commandContainer)
		jda = builder.build()
		isActive = true

		Runtime.getRuntime().addShutdownHook(Thread {
			isActive = false

			logger.info("shutdown now Px32 kernel v${DefaultConfig.version}")
			jda.shutdownNow()
		})

		return jda
	}
}

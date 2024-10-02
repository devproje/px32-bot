package net.projecttl.p.x32.api

import org.jetbrains.exposed.sql.Database
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.reflect.KProperty
import kotlin.system.exitProcess

object BotConfig {
	private fun useBotConfig(): BotConfigDelegate {
		return BotConfigDelegate()
	}

	fun useDatabase(): Database {
		if (db_username.isNotBlank() && db_password.isNotBlank()) {
			return Database.connect(
				driver = db_driver,
				url = db_url,
				user = db_username,
				password = db_password
			)
		}

		return Database.connect(
			driver = db_driver,
			url = db_url
		)
	}

	val token: String by useBotConfig()
	val owner: String by useBotConfig()

	private val bundle_func: String by useBotConfig()
	val bundle = when (bundle_func) {
		"1"  -> true
		"0"  -> false
		else -> {
			throw IllegalArgumentException("bundle_func option must be 0 or 1")
		}
	}

	private val db_driver: String by useBotConfig()
	private val db_url: String by useBotConfig()
	private val db_username: String by useBotConfig()
	private val db_password: String by useBotConfig()
}

private class BotConfigDelegate {
	private val props = Properties()

	init {
		val file = File("config.properties")
		if (!file.exists()) {
			val default = this.javaClass.getResourceAsStream("/config.sample.properties")!!.readBytes()
			file.outputStream().use { stream ->
				stream.write(default)
			}

			println("config.properties is not found, create new one...")
			exitProcess(1)
		}

		props.load(FileInputStream(file))
	}

	operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
		return props.getProperty(property.name).toString()
	}
}

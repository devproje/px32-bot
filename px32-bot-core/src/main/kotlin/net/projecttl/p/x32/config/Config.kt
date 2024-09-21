package net.projecttl.p.x32.config

import net.projecttl.p.x32.logger
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.reflect.KProperty
import kotlin.system.exitProcess

object Config {
	private fun useConfig(): ConfigDelegate {
		return ConfigDelegate()
	}

	val token: String by useConfig()
	val owner: String by useConfig()

	private val bundle_func: String by useConfig()
	val bundle = if (bundle_func == "1") true else if (bundle_func == "0") false else throw IllegalArgumentException("bundle_func option must be 0 or 1")

	val db_driver: String by useConfig()
	val db_url: String by useConfig()
	val db_username: String by useConfig()
	val db_password: String by useConfig()
}

private class ConfigDelegate {
	private val props = Properties()

	init {
		val file = File("config.properties")
		if (!file.exists()) {
			val default = this.javaClass.getResourceAsStream("/config.sample.properties")!!.readBytes()
			file.outputStream().use { stream ->
				stream.write(default)
			}

			logger.error("config.properties is not found, create new one...")
			exitProcess(1)
		}

		props.load(FileInputStream(file))
	}

	operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
		return props.getProperty(property.name).toString()
	}
}
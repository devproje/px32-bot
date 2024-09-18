package net.projecttl.p.x32.config

import java.util.*
import kotlin.reflect.KProperty

object DefaultConfig {
	private fun useConfig(): DefaultConfigDelegate {
		return DefaultConfigDelegate()
	}

	val version: String by useConfig()
}

private class DefaultConfigDelegate {
	private val props = Properties()

	init {
		props.load(this.javaClass.getResourceAsStream("/default.properties"))
	}

	operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
		return props.getProperty(property.name).toString()
	}
}

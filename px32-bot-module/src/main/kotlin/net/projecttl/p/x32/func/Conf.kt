package net.projecttl.p.x32.func

import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.reflect.KProperty

object Conf {
	private fun useDef(): DefDel {
		return DefDel()
	}

	private fun useConf(): ConfDel {
		return ConfDel()
	}

	val owner: String by useConf()
	val version: String by useDef()
}

private class ConfDel {
	private val props = Properties()

	init {
		val file = File("config.properties")
		props.load(FileInputStream(file))
	}

	operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
		return props.getProperty(property.name).toString()
	}
}

class DefDel {
	private val props = Properties()

	init {
		props.load(this.javaClass.getResourceAsStream("/default.properties"))
	}

	operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
		return props.getProperty(property.name).toString()
	}
}

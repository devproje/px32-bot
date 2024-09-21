import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.reflect.KProperty

object Conf {
	private fun useConf(): ConfDel {
		return ConfDel()
	}

	val owner: String by useConf()
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

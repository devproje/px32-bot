package net.projecttl.p.x32

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.projecttl.p.x32.command.Info
import net.projecttl.p.x32.command.PluginCommand
import net.projecttl.p.x32.command.Reload
import net.projecttl.p.x32.config.Config
import net.projecttl.p.x32.config.DefaultConfig
import net.projecttl.p.x32.kernel.CoreKernel
import org.jetbrains.exposed.sql.Database
import org.slf4j.Logger
import org.slf4j.LoggerFactory

lateinit var jda: JDA
lateinit var kernel: CoreKernel
lateinit var database: Database

val logger: Logger = LoggerFactory.getLogger(Px32::class.java)

@OptIn(DelicateCoroutinesApi::class)
fun main() {
	println("Px32 version v${DefaultConfig.version}")
	if (Config.owner.isBlank() || Config.owner.isEmpty()) {
		logger.warn("owner option is blank or empty!")
	}

	kernel = CoreKernel(Config.token)
	val handler = kernel.getCommandContainer()

	handler.addCommand(Info)
	handler.addCommand(Reload)
	handler.addCommand(PluginCommand)

	jda = kernel.build()
	GlobalScope.launch {
		kernel.register(jda)
	}
}

object Px32

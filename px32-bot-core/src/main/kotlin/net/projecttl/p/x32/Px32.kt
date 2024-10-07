package net.projecttl.p.x32

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.projecttl.p.x32.api.BotConfig
import net.projecttl.p.x32.command.Info
import net.projecttl.p.x32.command.PluginCommand
import net.projecttl.p.x32.command.Reload
import net.projecttl.p.x32.config.DefaultConfig
import net.projecttl.p.x32.kernel.CoreKernel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

lateinit var jda: JDA
	private set
lateinit var kernel: CoreKernel
	private set

val logger: Logger = LoggerFactory.getLogger(Px32::class.java)

@OptIn(DelicateCoroutinesApi::class)
fun main(args: Array<out String>) {
	println("Px32 version v${DefaultConfig.version}")
	if (BotConfig.owner.isBlank() || BotConfig.owner.isEmpty()) {
		logger.warn("owner option is blank or empty!")
	}

	kernel = CoreKernel(BotConfig.token)
	val handler = kernel.commandContainer

	if (args.contains("--remove-cmd")) {
		jda = kernel.build()
		try {
			jda.retrieveCommands().queue {
				if (it == null) {
					return@queue
				}

				it.forEach { command ->
					logger.info("unregister command: /${command.name}")
					command.jda.deleteCommandById(command.id).queue()
				}
			}
		} catch (ex: Exception) {
			ex.printStackTrace()
		}

		kernel.kill()
		return
	}

	handler.addCommand(Info)
	handler.addCommand(Reload)
	handler.addCommand(PluginCommand)

	jda = kernel.build()
	GlobalScope.launch {
		kernel.register(jda)
	}
}

object Px32

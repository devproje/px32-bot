package net.projecttl.p.x32.command

import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.projecttl.p.x32.api.command.GlobalCommand
import net.projecttl.p.x32.api.command.useCommand
import net.projecttl.p.x32.config.DefaultConfig
import net.projecttl.p.x32.kernel
import java.lang.management.ManagementFactory

object Info : GlobalCommand {
	override val data: CommandData = useCommand {
		name = "info"
		description = "봇의 정보를 표시 합니다"
	}

	override suspend fun execute(ev: SlashCommandInteractionEvent) {
		val rb = ManagementFactory.getRuntimeMXBean()
		val r = Runtime.getRuntime()

		val size = kernel.plugins.size
		val info = """
			Px32Bot v${DefaultConfig.version}, JDA `v${JDAInfo.VERSION}`,
			`Java ${System.getProperty("java.version")}` and `Kotlin ${KotlinVersion.CURRENT}` System on `${System.getProperty("os.name")}`
			
			Process Started on <t:${(System.currentTimeMillis() - rb.uptime)/ 1000L}:R>
			Bot Process Running on PID `${rb.pid}`
			
			Assigned `${r.maxMemory() / 1048576}MB` of Max Memories at this Bot
			Using `${(r.totalMemory() - r.freeMemory()) / 1048576}MB` at this Bot
			
			Total $size plugin${if (size > 1) "s" else ""} loaded
        """.trimIndent()

		ev.reply(info).queue()
	}
}
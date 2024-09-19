package net.projecttl.p.x32

import net.dv8tion.jda.api.JDA
import net.projecttl.p.x32.config.DefaultConfig
import net.projecttl.p.x32.func.loadDefault
import net.projecttl.p.x32.handler.Ready
import net.projecttl.p.x32.kernel.CoreKernel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

lateinit var jda: JDA
val logger: Logger = LoggerFactory.getLogger(Px32::class.java)

fun main() {
	println("Px32 version v${DefaultConfig.version}")
	val kernel = CoreKernel(System.getenv("TOKEN"))
	kernel.addHandler(Ready)

	val handler = kernel.getGlobalCommandHandler()
	loadDefault(handler)

	jda = kernel.build()
}

object Px32

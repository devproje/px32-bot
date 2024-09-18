package net.projecttl.p.x32

import net.projecttl.p.x32.command.Avatar
import net.projecttl.p.x32.command.Ping
import net.projecttl.p.x32.config.DefaultConfig
import net.projecttl.p.x32.handler.Ready
import net.projecttl.p.x32.kernel.CoreKernel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger(Px32::class.java)

fun main() {
	logger.info("PX32 version v${DefaultConfig.version}")
	val kernel = CoreKernel(System.getenv("TOKEN"))
	kernel.addHandler(Ready)

	val handler = kernel.getGlobalCommandHandler()
	handler.addCommand(Avatar)
	handler.addCommand(Ping)

	kernel.build()
}

class Px32

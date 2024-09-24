package net.projecttl.p.x32.func

import net.projecttl.p.x32.api.Plugin
import net.projecttl.p.x32.api.command.commandHandler
import net.projecttl.p.x32.func.command.*
import net.projecttl.p.x32.func.handler.Ready

lateinit var instance: BundleModule

class BundleModule : Plugin() {
    override fun onLoad() {
        instance = this

        logger.info("Created by Project_IO")
        logger.info("Hello! This is Px32's general module!")

        addHandler(Ready)
        addHandler(commandHandler { handler ->
            handler.addCommand(Avatar)
            handler.addCommand(Bmi)
            handler.addCommand(Dice)
            handler.addCommand(MsgLength)
            handler.addCommand(MsgPurge)
            handler.addCommand(Ping)
        })
    }

    override fun destroy() {
        logger.info("bye!")
    }
}



package net.projecttl.p.x32.func

import net.dv8tion.jda.api.JDABuilder
import net.projecttl.p.x32.api.Plugin
import net.projecttl.p.x32.api.command.CommandHandler
import net.projecttl.p.x32.func.command.Avatar
import net.projecttl.p.x32.func.command.MsgPurge
import net.projecttl.p.x32.func.command.MsgLength
import net.projecttl.p.x32.func.command.Ping
import net.projecttl.p.x32.func.handler.Ready


class General : Plugin() {
    override fun onLoad() {
        logger.info("Created by Project_IO")
        logger.info("Hello! This is Px32's general module!")

        addHandler(Ready)
        addHandler(with(CommandHandler()) {
            addCommand(Avatar)
            addCommand(MsgLength)
            addCommand(Ping)
            addCommand(MsgPurge)

            this
        })
    }

    override fun destroy() {
        logger.info("bye!")
    }
}



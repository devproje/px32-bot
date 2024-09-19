package net.projecttl.p.x32.func

import net.projecttl.p.x32.api.command.CommandHandler
import net.projecttl.p.x32.func.command.Avatar
import net.projecttl.p.x32.func.command.Ping

fun loadDefault(handler: CommandHandler) = with(handler) {
    addCommand(Avatar)
    addCommand(Ping)
}

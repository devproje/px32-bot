package net.projecttl.p.x32.func.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import net.projecttl.p.x32.api.command.GlobalCommand
import kotlin.random.Random
import kotlin.random.nextInt

object Dice : GlobalCommand {
	override val data = CommandData.fromData(CommandDataImpl("dice", "랜덤으로 주사위를 굴립니다").toData())

	override suspend fun execute(ev: SlashCommandInteractionEvent) {
		val rand = Random.nextInt(1..6)
		ev.reply(":game_die: **${rand}**").queue()
	}
}

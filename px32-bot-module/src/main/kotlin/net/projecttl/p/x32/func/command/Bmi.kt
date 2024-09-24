package net.projecttl.p.x32.func.command

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import net.projecttl.p.x32.api.command.GlobalCommand
import net.projecttl.p.x32.api.util.colour
import net.projecttl.p.x32.api.util.footer
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow

object Bmi : GlobalCommand {
	override val data = CommandData.fromData(CommandDataImpl("bmi", "키와 몸무게 기반으로 bmi지수를 계산할 수 있어요").apply {
		addOption(OptionType.NUMBER, "height", "신장 길이를 적어 주세요 (cm)", true)
		addOption(OptionType.NUMBER, "weight", "몸무게를 적어 주세요 (kg)", true)
	}.toData())

	override suspend fun execute(ev: SlashCommandInteractionEvent) {
		val height = ev.getOption("height")!!.asDouble
		val weight = ev.getOption("weight")!!.asDouble

		if (height <= 0.0 || weight <= 0.0) {
			ev.reply(":warning: 키 또는 몸무게가 0 또는 음수일 수 없어요").setEphemeral(true).queue()
			return
		}

		val bmi = weight / (height / 100).pow(2)
		fun result(bmi: Double): String {
			return when {
				bmi < 18.5 				-> "**저체중**"
				bmi in 18.5..24.9 -> "**정상 체중**"
				bmi in 25.0.. 29.9 	-> "**과체중**"
				else -> "**비만**"
			}
		}

		val df = DecimalFormat("#.##")
		df.roundingMode = RoundingMode.HALF_UP

		ev.replyEmbeds(EmbedBuilder().apply {
			setTitle(":pencil: BMI 지수가 나왔어요")
			setDescription("${result(bmi)} 이에요")
			addField(":straight_ruler: **신장 길이**", "`${height}cm`", true)
			addField(":scales: **몸무게**", "`${weight}kg`", true)
			addField(":chart_with_downwards_trend: **BMI 지수**", "`${df.format(bmi)}` ", true)

			colour()
			footer(ev.user)
		}.build()).queue()
	}
}
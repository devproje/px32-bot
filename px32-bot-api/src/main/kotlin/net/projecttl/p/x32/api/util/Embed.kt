package net.projecttl.p.x32.api.util

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.User
import java.time.LocalDateTime
import kotlin.random.Random

fun EmbedBuilder.colour(): EmbedBuilder {
	val rand = Random.nextInt(0x000001, 0xffffff)
	return this.setColor(rand)
}

fun EmbedBuilder.footer(user: User): EmbedBuilder {
	val date = LocalDateTime.now()
	val mon = if (date.month.value > 9) {
		date.month.value
	} else {
		"0${date.month.value}"
	}
	val str = "${user.name} • ${date.year}-${mon}-${date.dayOfMonth} ${date.hour}:${date.minute}"

	return this.setFooter(str, "${user.avatarUrl}?size=1024")
}

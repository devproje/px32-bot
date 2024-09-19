package net.projecttl.p.x32.util

import net.dv8tion.jda.api.EmbedBuilder
import kotlin.random.Random

fun EmbedBuilder.colour(): EmbedBuilder {
	val rand = Random.nextInt(0x000001, 0xffffff)
	return this.setColor(rand)
}

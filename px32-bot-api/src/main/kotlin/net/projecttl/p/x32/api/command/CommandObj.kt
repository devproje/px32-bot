package net.projecttl.p.x32.api.command

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData
import net.dv8tion.jda.internal.interactions.CommandDataImpl

fun useCommand(init: CommandObj.() -> Unit): CommandData {
	val obj = CommandObj()
	init.invoke(obj)

	return obj.build()
}

class CommandObj {
	var name: String = ""
	var description: String = ""

	val subcommands = mutableListOf<SubcommandData>()
	val subcommandGroups = mutableListOf<SubcommandGroupData>()
	val options = mutableListOf<OptionData>()

	fun subcommand(sub: SubcommandObj.() -> Unit) {
		val obj = SubcommandObj()
		sub.invoke(obj)

		subcommands += obj.build()
	}

	fun subcommandGroup(group: SubcommandGroupObj.() -> Unit) {
		val obj = SubcommandGroupObj()
		group.invoke(obj)

		subcommandGroups += obj.build()
	}

	fun option(opt: OptionObj.() -> Unit) {
		val obj = OptionObj()
		opt.invoke(obj)

		options += obj.build()
	}

	fun build(): CommandData {
		val obj = CommandDataImpl(name, description)
		if (subcommands.isNotEmpty()) {
			subcommands.forEach {
				obj.addSubcommands(it)
			}
		}

		if (subcommandGroups.isNotEmpty()) {
			subcommandGroups.forEach {
				obj.addSubcommandGroups(it)
			}
		}

		if (options.isNotEmpty()) {
			options.forEach {
				obj.addOptions(it)
			}
		}

		return CommandData.fromData(obj.toData())
	}
}

class SubcommandObj {
	var name: String = ""
	var description: String = ""
	val options = mutableListOf<OptionData>()

	fun option(opt: OptionObj.() -> Unit) {
		val obj = OptionObj()
		opt.invoke(obj)

		options += obj.build()
	}

	fun build(): SubcommandData {
		val obj = SubcommandData(name, description)
		if (options.isNotEmpty()) {
			options.forEach {
				obj.addOptions(it)
			}
		}

		return obj
	}
}

class SubcommandGroupObj {
	var name: String = ""
	var description: String = ""
	val subcommands = mutableListOf<SubcommandData>()

	fun subcommand(opt: SubcommandObj.() -> Unit) {
		val obj = SubcommandObj()
		opt.invoke(obj)

		subcommands += obj.build()
	}

	fun build(): SubcommandGroupData {
		val obj = SubcommandGroupData(name, description)
		if (subcommands.isNotEmpty()) {
			subcommands.forEach {
				obj.addSubcommands(it)
			}
		}

		return obj
	}
}

class OptionObj {
	lateinit var type: OptionType
	var name: String = ""
	var description: String = ""
	var required: Boolean = false
	var autoComplete: Boolean = false

	fun build(): OptionData {
		return OptionData(type, name, description, required, autoComplete)
	}
}

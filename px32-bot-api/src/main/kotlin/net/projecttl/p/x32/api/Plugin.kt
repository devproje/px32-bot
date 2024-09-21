package net.projecttl.p.x32.api

import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.projecttl.p.x32.api.command.CommandHandler
import net.projecttl.p.x32.api.model.PluginConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Plugin {
    private val handlerContainer = mutableListOf<ListenerAdapter>()
    private val config = this.javaClass.getResourceAsStream("/plugin.json")?.let {
        val raw = it.bufferedReader().readText()
        val obj = Json.decodeFromString<PluginConfig>(raw)

        return@let obj
    }

    fun getLogger(): Logger {
        return LoggerFactory.getLogger(config?.name)
    }

    fun getHandlers(): List<ListenerAdapter> {
        return handlerContainer
    }

    fun addHandler(listener: ListenerAdapter) {
        handlerContainer.add(listener)
    }

    fun delHandler(listener: ListenerAdapter) {
        handlerContainer.remove(listener)
    }

    abstract fun onLoad()
    abstract fun destroy()
}

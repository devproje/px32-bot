package net.projecttl.p.x32.api

import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.projecttl.p.x32.api.model.PluginConfig
import net.projecttl.p.x32.api.util.AsyncTaskContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Plugin {
    private val handlerContainer = mutableListOf<ListenerAdapter>()
    val taskContainer = AsyncTaskContainer()

    val config = this.javaClass.getResourceAsStream("/plugin.json")!!.let {
        val raw = it.bufferedReader().readText()
        val obj = Json.decodeFromString<PluginConfig>(raw)

        return@let obj
    }

    var logger: Logger = LoggerFactory.getLogger(this::class.java)

    val handlers: List<ListenerAdapter>
        get() = handlerContainer

    fun addHandler(listener: ListenerAdapter) {
        handlerContainer += listener
    }

    fun delHandler(listener: ListenerAdapter) {
        handlerContainer -= listener
    }

    abstract fun onLoad()
    abstract fun destroy()
}

package net.projecttl.p.x32.kernel

import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.projecttl.p.x32.api.Plugin
import net.projecttl.p.x32.api.model.PluginConfig
import net.projecttl.p.x32.logger
import java.io.File
import java.net.URLClassLoader
import java.nio.charset.Charset
import java.util.jar.JarFile

object PluginLoader {
    private val plugins = mutableMapOf<PluginConfig, Plugin>()
    private val parentDir = File("./plugins").apply {
        if (!exists()) {
            mkdirs()
        }
    }

    fun getPlugins(): Map<PluginConfig, Plugin> {
        return plugins.toMap()
    }

    fun load() {
        parentDir.listFiles()?.forEach { file ->
            loadPlugin(file)
        }
    }

    fun destroy() {
        plugins.forEach { (config, plugin) ->
            logger.info("disable ${config.name} plugin...")

            try {
                plugin.destroy()
            } catch (ex: Exception) {
                logger.error("failed to destroy ${config.name} plugin")
                ex.printStackTrace()
            }
        }
    }

    private fun loadPlugin(file: File) {
        if (!file.name.endsWith(".jar")) {
            return
        }

        val jar = JarFile(file)
        val cnf = jar.entries().toList().singleOrNull { jarEntry -> jarEntry.name == "plugin.json" }
        if (cnf == null)
            return logger.error("${file.name} is not a plugin. aborted")

        val stream = jar.getInputStream(cnf)
        val raw = stream.use {
            return@use it.readBytes().toString(Charset.forName("UTF-8"))
        }

        val config = Json.decodeFromString<PluginConfig>(raw)
        val cl = URLClassLoader(arrayOf(file.toPath().toUri().toURL()))
        val obj = cl.loadClass(config.main).getDeclaredConstructor().newInstance()

        if (obj !is Plugin)
            return logger.error("${config.name} is not valid main class. aborted")

        try {
            logger.info("Load plugin ${config.name} v${config.version}")
            obj.onLoad()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return logger.error("Failed to load plugin ${config.name}")
        }

        plugins[config] = obj
    }
}

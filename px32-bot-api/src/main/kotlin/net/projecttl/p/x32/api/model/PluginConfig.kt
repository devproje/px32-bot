package net.projecttl.p.x32.api.model

import kotlinx.serialization.Serializable

@Serializable
data class PluginConfig(
    val name: String,
    val version: String,
    val main: String
)

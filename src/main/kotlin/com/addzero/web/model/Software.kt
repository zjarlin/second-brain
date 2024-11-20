package com.addzero.web.model

import kotlinx.serialization.Serializable

@Serializable
data class Software(
    val id: String,
    val name: String,
    val version: String,
    val platform: String,
    val supportedOs: List<String>,
    val downloads: Long,
    val updateTime: Long,
    val description: String,
    val category: String,
    val downloadUrl: String,
    val iconUrl: String? = null
) 
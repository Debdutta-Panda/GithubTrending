package com.learnpa.models

import kotlinx.serialization.Serializable

@Serializable
data class EndPoint(
    val baseUrl: String
)
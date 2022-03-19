package com.learnpea.models
import kotlinx.serialization.Serializable
@Serializable
data class EndPointResponse(
    val success: Boolean,
    val message: String,
    val endPoint: EndPoint?
)

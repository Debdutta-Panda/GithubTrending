package com.learnpea.models
import kotlinx.serialization.Serializable
@Serializable
data class ContentItem(
    val id: String,
    val content: String
)

@Serializable
data class ContentItemsResponse(
    val success: Boolean,
    val message: String,
    val contentItems: List<ContentItem>?
)
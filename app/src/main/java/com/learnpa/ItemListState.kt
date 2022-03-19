package com.learnpa

import com.learnpea.models.ContentItem

data class ItemListState(
    val isLoading: Boolean = false,
    val items: List<ContentItem> = emptyList(),
    val error: String = ""
)
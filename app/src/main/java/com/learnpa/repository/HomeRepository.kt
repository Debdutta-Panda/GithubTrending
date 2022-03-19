package com.learnpa.repository

import com.learnpa.Resource
import com.learnpa.models.ContentItemsResponse
import com.learnpa.models.Response

interface HomeRepository {
    suspend fun getContents(): Response<ContentItemsResponse>
}
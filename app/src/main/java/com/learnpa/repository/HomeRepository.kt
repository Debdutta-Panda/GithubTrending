package com.learnpa.repository

import com.learnpa.Resource
import com.learnpa.models.*

interface HomeRepository {
    suspend fun getContents(): Response<ContentItemsResponse>
    suspend fun languages(): Response<List<Language>>
    suspend fun repositories(): Response<List<Repository>>
}
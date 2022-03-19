package com.learnpea.repository

import com.learnpea.models.ContentItemsResponse
import com.learnpea.models.Repository
import com.learnpea.models.Response
import org.intellij.lang.annotations.Language

interface HomeRepository {
    suspend fun repositories(): Response<List<Repository>>
}
package com.learnpa.repository_impl

import com.learnpa.Resource
import com.learnpa.models.ContentItemsResponse
import com.learnpa.models.Response
import com.learnpa.repository.HomeRepository
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val dataStore: com.learnpa.repository.DataStore,
) : HomeRepository {
    override suspend fun getContents(): Response<ContentItemsResponse> {
        val baseUrl = dataStore.getBaseUrl()
        return try {
            Response(client.get {
                url("$baseUrl/getHomeContents")
            },null)
        } catch (e: Exception) {
            Response(null, e)
        }
    }
}
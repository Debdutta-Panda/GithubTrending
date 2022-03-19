package com.learnpa.repository_impl

import com.learnpa.Resource
import com.learnpa.models.*
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

    override suspend fun languages(): Response<List<Language>>{
        return try {
            Response(client.get{
                url("https://github-trending.p.rapidapi.com/languages")
                header("x-rapidapi-host", "github-trending.p.rapidapi.com")
                header("x-rapidapi-key", "26a67c77b7mshea8eea1d7874b9dp1d5efejsnea1915fba247")
            },null)
        } catch (e: Exception) {
            Response(null, e)
        }
    }

    override suspend fun repositories(): Response<List<Repository>>{
        return try {
            Response(client.get{
                url("https://github-trending.p.rapidapi.com/repositories")
                header("x-rapidapi-host", "github-trending.p.rapidapi.com")
                header("x-rapidapi-key", "26a67c77b7mshea8eea1d7874b9dp1d5efejsnea1915fba247")
            },null)
        } catch (e: Exception) {
            Response(null, e)
        }
    }
}
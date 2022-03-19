package com.learnpa.repository_impl

import com.learnpa.models.EndPointResponse
import com.learnpa.models.Language
import com.learnpa.repository.SplashRepository
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : SplashRepository {
    override suspend fun getEndPoint(): EndPointResponse? {
        return try {
            client.get {
                url("https://app.learnpea.com/api/baseUrl")
            }
        } catch (e: Exception) {
            null
        }
    }
    override suspend fun languages(): List<Language>{
        return client.get{
            url("https://github-trending.p.rapidapi.com/languages")
            header("x-rapidapi-host", "github-trending.p.rapidapi.com")
            header("x-rapidapi-key", "26a67c77b7mshea8eea1d7874b9dp1d5efejsnea1915fba247")
        }
    }
}
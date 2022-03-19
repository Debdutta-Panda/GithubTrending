package com.learnpa.repository

import com.learnpa.Constants
import com.learnpa.Metar
import com.learnpea.models.*
import com.learnpea.repository.HomeRepository
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val client: HttpClient,
) : HomeRepository {

    override suspend fun repositories(): Response<List<Repository>>{
        return try {
            Response(client.get{
                url(Metar[Constants.REPOSITORIES_END_KEY])
                header(Constants.API_HOST_KEY, Constants.API_HOST)
                header(Constants.API_HOST_API_KEY_KEY, Constants.API_HOST_API_KEY)
            },null)
        } catch (e: Exception) {
            Response(null, e)
        }
    }
}
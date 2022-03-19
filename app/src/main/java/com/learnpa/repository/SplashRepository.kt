package com.learnpa.repository

import com.learnpa.models.EndPointResponse
import com.learnpa.models.Language

interface SplashRepository {

    suspend fun languages(): List<Language>
    suspend fun getEndPoint(): EndPointResponse?
}
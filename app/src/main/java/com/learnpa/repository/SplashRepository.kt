package com.learnpa.repository

import com.learnpa.models.EndPointResponse
import com.learnpa.models.Language

interface SplashRepository {

    suspend fun getEndPoint(): EndPointResponse?
}
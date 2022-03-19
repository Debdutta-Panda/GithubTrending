package com.learnpa.usecases

import android.util.Log
import com.learnpa.repository.HomeRepository
import com.learnpa.Resource
import com.learnpa.models.ContentItem
import com.learnpa.models.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    fun getContents(): Flow<Resource<List<Repository>>> = flow {
        emit(Resource.Loading())
        val response = repository.repositories()
        val repositories = response.data
        if(repositories!=null){
            emit(Resource.Success(repositories))
        }
        else{
            emit(Resource.Error(""))
        }
    }
}
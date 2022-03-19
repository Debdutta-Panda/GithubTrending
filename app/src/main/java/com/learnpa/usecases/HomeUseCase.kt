package com.learnpa.usecases

import com.learnpa.repository.HomeRepository
import com.learnpa.Resource
import com.learnpa.models.ContentItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    fun getContents(): Flow<Resource<List<ContentItem>>> = flow {
        emit(Resource.Loading())
        //delay(5000)
        val response = repository.getContents()
        val contentItems = response.data?.contentItems
        if(contentItems!=null){
            emit(Resource.Success(contentItems))
        }
        else{
            emit(Resource.Error(""))
        }
    }
}
package com.learnpea.domain.usecases

import com.learnpea.Resource
import com.learnpea.repository.HomeRepository
import com.learnpea.models.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    fun getContents(): Flow<Resource<List<Repository>>> = flow {
        emit(Resource.Loading())
        delay(2000)
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
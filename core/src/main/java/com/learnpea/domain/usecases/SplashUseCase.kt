package com.learnpea.domain.usecases


import com.learnpea.models.Action
import com.learnpea.models.Command
import com.learnpea.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SplashUseCase @Inject constructor(
) {

    operator fun invoke(): Flow<Command> = flow {
        emit(Command(Action.LOADING))
        delay(3000)
        emit(Command(Action.GO_TO_PAGE, Routes.Home))
    }
}
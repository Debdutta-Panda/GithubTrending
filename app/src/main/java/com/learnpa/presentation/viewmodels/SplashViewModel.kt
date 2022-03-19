
package com.learnpa.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnpa.*
import com.learnpea.models.Action
import com.learnpea.domain.usecases.SplashUseCase
import com.learnpea.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import nl.frank.vmnc.ui.nav.RouteNavigator
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val splashUseCase: SplashUseCase
) : ViewModel(), RouteNavigator by routeNavigator {

    private val _state = mutableStateOf(ItemListState())
    val state: State<ItemListState> = _state

    val _splashState = mutableStateOf("")
    val splashState: State<String> = _splashState

    init {
        getBaseUrl()
    }

    private fun getBaseUrl() {
        splashUseCase().onEach { result ->
            when(result.action){
                Action.LOADING -> {
                    _splashState.value = R.string.loading.string
                }
                Action.OFFLINE -> {
                    _splashState.value = R.string.you_are_offline.string
                }
                Action.SUCCESS -> {
                    _splashState.value = R.string.yay.string
                }
                Action.VALIDATING -> {
                    _splashState.value = R.string.just_a_minute.string
                }
                Action.FAILED -> {
                    _splashState.value = R.string.something_went_wrong.string
                }
                Action.ONLINE -> {
                    _splashState.value = R.string.great_you_are_online.string
                }
                Action.RETRYING -> {
                    _splashState.value = R.string.let_me_look_into_that.string
                }
                Action.DONE -> {
                    _splashState.value = R.string.great_job.string
                }
                Action.GO_TO_PAGE -> {
                    routeNavigator.navigateToRoute((result.target as? String)?: Routes.Splash)
                }
            }
        }.launchIn(viewModelScope)
    }
}
package com.learnpa.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnpa.usecases.HomeUseCase
import com.learnpa.Resource
import com.learnpa.models.ContentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import nl.frank.vmnc.ui.nav.RouteNavigator
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val getHomeContentUseCase: HomeUseCase
) : ViewModel(), RouteNavigator by routeNavigator {
    private var homeContentPending = false
    enum class PageState{
        UNKNOWN,
        LOADING,
        DATA,
        ERROR,
    }
    private val _pageState = mutableStateOf(PageState.UNKNOWN)
    val pageState: State<PageState> = _pageState

    val contents = mutableStateListOf<ContentItem>()

    init {
        getHomeContent()
    }

    private fun getHomeContent() {
        getHomeContentUseCase.getContents().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _pageState.value = PageState.DATA
                    contents.addAll(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _pageState.value = PageState.ERROR
                    homeContentPending = true
                }
                is Resource.Loading -> {
                    _pageState.value = PageState.LOADING
                }
            }
        }.launchIn(viewModelScope)
    }

    fun popBackStack() {
        routeNavigator.navigateUp()
    }

    fun onTryAgainClicked() {
        getHomeContent()
    }
}
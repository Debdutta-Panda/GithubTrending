package com.learnpa.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnpa.usecases.HomeUseCase
import com.learnpa.Resource
import com.learnpa.models.ContentItem
import com.learnpa.models.Repository
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

    private val _searchMode = mutableStateOf(false)
    val searchMode: State<Boolean> = _searchMode

    val searchText = mutableStateOf("")
    val mainRepo = mutableStateListOf<Repository>()
    val contents = mutableStateListOf<Repository>()

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

    fun onSearchOrCloseClick() {
        _searchMode.value = !_searchMode.value
    }

    fun onSearch() {
        _searchMode.value = false
        val text = searchText.value.lowercase()
        mainRepo.clear()
        mainRepo.addAll(contents)
        contents.clear()
        val filtered = mainRepo.filter {
            val name = it.name?:"".lowercase()
            val author = it.author?:"".lowercase()
            val builtBys = it.builtBy.map {
                it.username?:"".lowercase()
            }
            name.contains(text)||author.contains(text)||builtBys.contains(text)
        }
        contents.addAll(filtered)
    }

    fun clearSearchText() {
        searchText.value = ""
    }
}
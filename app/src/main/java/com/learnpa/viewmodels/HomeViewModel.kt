package com.learnpa.viewmodels

import android.R.attr.label
import android.content.*
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnpa.Resource
import com.learnpa.models.Repository
import com.learnpa.usecases.HomeUseCase
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
                    var id = 0
                    contents.addAll(result.data?.apply {
                        forEach {
                            it.id = ++id
                        }
                    } ?: emptyList())
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
        if(!_searchMode.value&&isFiltered){
            contents.clear()
            contents.addAll(mainRepo)
            isFiltered = false
        }
    }

    var isFiltered = false
    fun onSearch() {
        if(isFiltered){
            contents.clear()
            contents.addAll(mainRepo)
        }
        isFiltered = false
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
        isFiltered = true
    }

    fun clearSearchText() {
        searchText.value = ""
    }

    val selected = mutableStateListOf<Int>()
    fun onItemClick(r: Repository) {
        if(selected.contains(r.id)){
            selected.remove(r.id)
        }
        else{
            selected.add(r.id)
        }
    }

    fun cancelSelectionMode() {
        selected.clear()
    }

    fun getUrlsToShare(): String{
        return contents.filter {
            selected.contains(it.id)
        }.map {
            it.url
        }.joinToString("\n")
    }

    fun forwardSelected(context: Context) {
        val urls = getUrlsToShare()
        selected.clear()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        //whatsappIntent.setPackage("com.whatsapp")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Github Trending Repos")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Here are my favourite Github repos:\n$urls")
        shareIntent.putExtra(Intent.EXTRA_TITLE, "Github Trending Repos")
        try {
            context.startActivity(Intent.createChooser(shareIntent, "Share via"));
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, "No app found to share", Toast.LENGTH_SHORT).show()
        }
    }

    fun copySelected(context: Context) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("Github Trending Repos", getUrlsToShare())
        selected.clear()
        clipboard?.apply {
            setPrimaryClip(clip)
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }
}
package com.learnpa

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.learnpea.models.Repository
import com.learnpa.presentation.ui.theme.TopBarBackground
import com.learnpa.presentation.viewmodels.HomeViewModel
import com.learnpea.Routes
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import nl.frank.vmnc.ui.nav.NavRoute

object HomeRoute : NavRoute<HomeViewModel> {

    override val route = Routes.Home

    @Composable
    override fun viewModel(): HomeViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: HomeViewModel) = HomePage(viewModel)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePage(
    viewModel: HomeViewModel
){
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    Scaffold(
        drawerBackgroundColor = Color.White,
        backgroundColor = Color.White,
        scaffoldState = scaffoldState.apply {

        },
        topBar = {
            TopAppBar(
                modifier = Modifier.height(80.dp),
                title = {
                    TitleContent(viewModel)
                },
                backgroundColor = TopBarBackground,
                navigationIcon = {
                    IconButton(onClick = {
                        if(viewModel.selected.size>0){
                            viewModel.cancelSelectionMode()
                        }else{
                            scope.launch {
                                scaffoldState.drawerState.apply {
                                    if (isClosed) {
                                        //open()// default way// use the following for full control
                                        animateTo(DrawerValue.Open, TweenSpec(durationMillis = 1000))
                                    } else{
                                        //close
                                        animateTo(DrawerValue.Closed, TweenSpec(durationMillis = 1000))
                                    }
                                }
                            }
                        }
                    }) {
                        if(viewModel.selected.size>0){
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Back"
                            )
                        }
                        else{
                            /*Icon(
                                imageVector = Icons.Filled.Menu,
                                tint = Color.White,
                                contentDescription = "Menu"
                            )*/
                        }
                    }
                }
            )
        },
        /*drawerContent = {

        },*/
        content = {
            HomePageContent(viewModel)
        },

    )
    val context = LocalContext.current
    BackHandler(enabled = true) {
        (context as? Activity)?.finish()
    }
}

@Composable
fun HomePageContent(viewModel: HomeViewModel) {
    when(viewModel.pageState.value){
        HomeViewModel.PageState.UNKNOWN -> {

        }
        HomeViewModel.PageState.LOADING -> {
            LottieView(
                R.raw.download_animation,
                modifier = Modifier.fillMaxSize()
            )
        }
        HomeViewModel.PageState.DATA -> {
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(12.dp)){
                items(viewModel.contents){
                    RepoRow(viewModel,it)
                }
            }
        }
        HomeViewModel.PageState.ERROR -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                LottieView(
                    R.raw.not_found_error,
                    modifier = Modifier.align(Alignment.Center)
                )
                Text(
                    R.string.try_again.string,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                        .clickable {
                            viewModel.onTryAgainClicked()
                        },
                    textDecoration = TextDecoration.Underline,
                    color = Color(0xff0366fc)
                )
            }
        }
    }
}

@Composable
fun RepoRow(viewModel: HomeViewModel, repo: Repository) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(12.dp)
            .clickable {
                viewModel.onItemClick(repo)
            },
        backgroundColor = if(viewModel.selected.contains(repo.id)) Color(0xfffcebff) else Color.White
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            RepoHeader(viewModel,repo)
            RepoDivider()
            RepoFooter(viewModel,repo)
        }
    }
}

@Composable
fun RepoHeader(viewModel: HomeViewModel, repo: Repository) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        RepoAvatar(repo)
        Spacer(modifier = Modifier.width(8.dp))
        RepoShortDetails(viewModel,repo)
    }
}

@Composable
fun RepoShortDetails(viewModel: HomeViewModel, repo: Repository) {
    Column(

    ) {
        RepoTitleRow(viewModel,repo)

        Text(
            repo.author?:stringResource(R.string.not_found)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            repo.description?:stringResource(R.string.no_description),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
fun RepoTitleRow(viewModel: HomeViewModel, repo: Repository) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            repo.name?: stringResource(R.string.not_found),
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        /*IconButton(onClick = {

        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Details"
            )
        }*/
    }
}

@Composable
fun RepoAvatar(repo: Repository) {
    GlideImage(
        imageModel = repo.avatar,
        contentScale = ContentScale.Inside,
        modifier = Modifier.size(100.dp)
    )
}

@Composable
fun RepoDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        thickness = 0.5.dp
    )
}

@Composable
fun RepoFooter(viewModel: HomeViewModel, repo: Repository) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            RepoMeta(viewModel,repo)
            RepoBuiltBy(viewModel,repo)
        }
    }
}

@Composable
fun RepoMeta(viewModel: HomeViewModel, repo: Repository) {
    Row(){
        RepoLanguage(viewModel,repo)
        RepoStars(viewModel,repo)
        RepoForks(viewModel,repo)
    }
}

@Composable
fun RepoForks(viewModel: HomeViewModel, repo: Repository) {
    repo.forks?.let { forks->
        Image(
            painter = painterResource(id = R.drawable.ic_fork_svgrepo_com),
            contentDescription = "Stars",
            modifier = Modifier.size(24.dp)
        )
        Text(forks.toString())
        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
fun RepoStars(viewModel: HomeViewModel, repo: Repository) {
    repo.stars?.let{ stars->
        Icon(
            imageVector = Icons.Filled.Star,
            tint = Color(0xffffb300),
            contentDescription = "Stars",
        )
        Text(stars.toString())
        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
fun RepoLanguage(viewModel: HomeViewModel, repo: Repository) {
    repo.language?.let { language->
        Text(
            language,
            color = Color.parse(repo.languageColor?:""),
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
fun RepoBuiltBy(viewModel: HomeViewModel, repo: Repository) {
    LazyRow(

    ){
        val builtByCount = repo.builtBy.size.coerceAtMost(2)
        val buildBys = repo.builtBy.take(builtByCount)
        items(buildBys){
            GlideImage(
                imageModel = it.avatar,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        val bdif = repo.builtBy.size - buildBys.size
        if(bdif>0){
            item {
                Text("+$bdif")
            }
        }
    }
}

@Composable
fun TitleContent(viewModel: HomeViewModel) {
    if(viewModel.selected.size>0){
        SelectedModeContent(viewModel)
    }
    else{
        SearchAndMenuContent(viewModel)
    }

}

@Composable
fun SelectedModeContent(viewModel: HomeViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            viewModel.selected.size.toString(),
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 30.sp
        )
        val context = LocalContext.current
        IconButton(onClick = {
            viewModel.copySelected(context)
        }) {
            Icon(
                imageVector = Icons.Filled.CopyAll,
                tint = Color.White,
                contentDescription = "Copy",
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(onClick = {
            viewModel.forwardSelected(context)
        }) {
            Icon(
                imageVector = Icons.Outlined.Share,
                tint = Color.White,
                contentDescription = "Forward",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun SearchAndMenuContent(viewModel: HomeViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        val focusRequester = FocusRequester()
        if(!viewModel.searchMode.value){
            Text(
                "Github Trending",
                color = Color.White
            )
        }
        else{
            val customTextSelectionColors = TextSelectionColors(
                handleColor = Color.Cyan,
                backgroundColor = Color.Cyan
            )
            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                TextField(

                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear",
                            tint = Color(0xff5796ff),
                            modifier = Modifier.clickable {
                                viewModel.clearSearchText()
                            }
                        )

                    },
                    value = viewModel.searchText.value,
                    onValueChange = {
                        viewModel.searchText.value = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color(0xffbad4ff),
                        cursorColor = Color.White,
                        backgroundColor = Color.Transparent,
                        textColor = Color.White,
                        placeholderColor = Color(0xffbad4ff),
                    ),
                    placeholder = {
                        Text(stringResource(R.string.repo_search_placeholder))
                    },
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.onSearch()
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    modifier = Modifier.focusRequester(focusRequester)
                )
            }
            SideEffect {
                focusRequester.requestFocus()
            }
        }
        if(!(viewModel.contents.size==0&&!viewModel.isFiltered)){
            IconButton(onClick = {
                viewModel.onSearchOrCloseClick()
            }) {
                Icon(
                    imageVector = if(!viewModel.searchMode.value) Icons.Outlined.Search else Icons.Outlined.Close,
                    tint = Color.White,
                    contentDescription = "Search",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

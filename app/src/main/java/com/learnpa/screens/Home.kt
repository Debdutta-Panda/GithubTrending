package com.learnpa

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.learnpa.ui.theme.TopBarBackground
import com.learnpa.viewmodels.HomeViewModel
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
                },
                backgroundColor = TopBarBackground,
                navigationIcon = {
                    IconButton(onClick = {
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
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            tint = Color.White,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        },
        drawerContent = {

        },
        content = {
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
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(12.dp)
                            ){
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ){
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                    ){
                                        GlideImage(
                                            imageModel = it.avatar,
                                            contentScale = ContentScale.Inside,
                                            modifier = Modifier.size(100.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(

                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ){
                                                Text(
                                                    it.name?: stringResource(R.string.not_found),
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 18.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f)
                                                )
                                                IconButton(onClick = {

                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Filled.KeyboardArrowRight,
                                                        contentDescription = "Details"
                                                    )
                                                }
                                            }

                                            Text(
                                                it.author?:stringResource(R.string.not_found)
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                it.description?:stringResource(R.string.no_description),
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                color = Color.Gray,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp, horizontal = 8.dp),
                                        thickness = 0.5.dp
                                    )
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
                                            Row(){
                                                it.language?.let {language->
                                                    Text(
                                                        language,
                                                        color = Color.parse(it.languageColor?:""),
                                                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                }
                                                it.stars?.let{stars->
                                                    Icon(
                                                        imageVector = Icons.Filled.Star,
                                                        tint = Color(0xffffb300),
                                                        contentDescription = "Stars",
                                                    )
                                                    Text(stars.toString())
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                }
                                                it.forks?.let {forks->
                                                    Image(
                                                        painter = painterResource(id = R.drawable.ic_fork_svgrepo_com),
                                                        contentDescription = "Stars",
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                    Text(forks.toString())
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                }
                                            }
                                            val builtByCount = it.builtBy.size.coerceAtMost(2)
                                            val buildBys = it.builtBy.take(builtByCount)
                                            LazyRow(

                                            ){
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
                                                val bdif = it.builtBy.size - buildBys.size
                                                if(bdif>0){
                                                    item {
                                                        Text("+$bdif")
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
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
        },

    )
    BackHandler(enabled = true) {
    }
}
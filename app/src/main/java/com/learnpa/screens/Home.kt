package com.learnpa

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.learnpa.app.MyApplication
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
                title = {
                    Text(
                        "Github Trending",
                        color = Color.White
                    )
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
                                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(12.dp)
                            ){
                                Row(
                                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                                ){
                                    GlideImage(
                                        imageModel = it.avatar,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(100.dp)
                                    )
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
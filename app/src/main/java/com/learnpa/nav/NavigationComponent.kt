package nl.frank.vmnc.ui.nav.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.learnpa.*
import com.learnpa.presentation.screens.SplashRoute

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationComponent(navHostController: NavHostController, paddingValues: PaddingValues) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = SplashRoute.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        SplashRoute.composable(this, navHostController)
        HomeRoute.composable(
            this, navHostController
        )
    }
}
package com.learnpa.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.ui.graphics.Color
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.learnpa.presentation.ui.theme.LearnPeaTheme
import dagger.hilt.android.AndroidEntryPoint
import nl.frank.vmnc.ui.nav.ui.NavigationComponent



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberAnimatedNavController()
            LearnPeaTheme {
                Scaffold(
                    contentColor = Color.Black
                ) {
                    NavigationComponent(navController, it)
                }
            }
        }
    }
}
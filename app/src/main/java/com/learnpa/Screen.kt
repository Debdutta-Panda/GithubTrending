package com.learnpa

sealed class Screen(val route: String) {
    object Splash: Screen("splash")
    object Page1: Screen("page1")
    object Page2: Screen("page2")
}
package com.learnpa

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
val duration = 1000
@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { duration },
        animationSpec = tween(duration)
    ) + fadeIn(animationSpec = tween(duration))
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { -duration },
        animationSpec = tween(duration)
    ) + fadeOut(animationSpec = tween(duration))
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { -duration },
        animationSpec = tween(duration)
    ) + fadeIn(animationSpec = tween(duration))
}


@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { duration },
        animationSpec = tween(duration)
    ) + fadeOut(animationSpec = tween(duration))
}
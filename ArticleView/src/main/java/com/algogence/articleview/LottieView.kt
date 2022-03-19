package com.algogence.articleview

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieView(
    @RawRes json: Int,
    iterations: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier
){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(json))
    LottieAnimation(
        composition,
        iterations = iterations,
        modifier = modifier,
    )
}

@Composable
fun LottieView(
    url: String,
    iterations: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier
){
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(url))
    LottieAnimation(
        composition,
        iterations = iterations,
        modifier = modifier,
    )
}
package com.learnpa.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.learnpa.*
import com.learnpa.R
import com.learnpa.viewmodels.SplashViewModel
import com.learnpea.Routes
import nl.frank.vmnc.ui.nav.NavRoute

object SplashRoute : NavRoute<SplashViewModel> {

    override val route = Routes.Splash

    @Composable
    override fun viewModel(): SplashViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: SplashViewModel) = SplashPage(viewModel)
}

@Composable
fun SplashPage(
    viewModel: SplashViewModel
) {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(R.drawable.ic_github_svgrepo_com),
                    contentDescription = "Github",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(24.dp)
                )
                LottieView(
                    json = R.raw.trending1,
                    iterations = Int.MAX_VALUE,
                    modifier = Modifier
                        .size(200.dp)
                )
                Text(
                    R.string.github_trending.string,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                )
            }
        }
    }

}

@Composable
fun BoxScope.Anim() {
    val configuration = LocalConfiguration.current
    LottieView(
        json = R.raw.learning,
        iterations = Int.MAX_VALUE,
        modifier = Modifier
            .size((configuration.screenWidthDp * 0.75).dp)
            .align(Alignment.TopCenter),
    )
}

@Composable
fun BoxScope.Logo(viewModel: SplashViewModel) {
    Column(modifier = Modifier
        .wrapContentSize()
        .align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            viewModel.splashState.value
        )
        Spacer(modifier = Modifier.height(12.dp))
        IconLogo()
        Constants.Dimens.SPLASH_LOGOS_GAP.SpaceX()
        TextLogo()
    }
}

@Composable
fun TextLogo() {
    Image(
        modifier = Modifier
            .width(Constants.Dimens.SPLASH_TEXT_LOGO_WIDTH.dp)
            .height(Constants.Dimens.SPLASH_TEXT_LOGO_HEIGHT.dp),
        painter = painterResource(id = Constants.Drawables.SPLASH_TEXT_LOGO),
        contentDescription = stringResource(R.string.logo),
    )
}

@Composable
fun IconLogo() {
    Image(
        modifier = Modifier
            .size(Constants.Dimens.SPLASH_LOGO_SIZE.dp),
        painter = painterResource(id = Constants.Drawables.SPLASH_LOGO),
        contentDescription = stringResource(R.string.logo)
    )
}

@Composable
fun BoxScope.Loader() {
    val configuration = LocalConfiguration.current
    LottieView(
        json = R.raw.rainbow_loader,
        iterations = Int.MAX_VALUE,
        modifier = Modifier
            .size(configuration.screenWidthDp.dp)
            .align(Alignment.BottomCenter),
    )
}

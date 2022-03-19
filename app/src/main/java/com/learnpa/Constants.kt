package com.learnpa

import androidx.compose.ui.text.font.Font

object Constants {

    const val REPOSITORIES_END_KEY = "repositories"
    const val API_HOST_KEY = "x-rapidapi-host"
    const val API_HOST_API_KEY_KEY = "x-rapidapi-key"
    const val API_HOST_API_KEY = "26a67c77b7mshea8eea1d7874b9dp1d5efejsnea1915fba247"
    const val API_HOST = "github-trending.p.rapidapi.com"

    object Dimens{
        const val SPLASH_LOGO_SIZE = 100
        const val SPLASH_TEXT_LOGO_WIDTH = 180
        const val SPLASH_TEXT_LOGO_HEIGHT = 30
        const val SPLASH_LOGOS_GAP = 24
    }

    object Drawables{
        const val SPLASH_LOGO = R.drawable.ic_learnpea_logo
        const val SPLASH_TEXT_LOGO = R.drawable.ic_learnpea_text_logo
    }

    object Fonts{
        val JustBubble = Font(R.raw.just_bubble)
        val Hubballi = Font(R.raw.hubballi)
        val NotoSans = Font(R.raw.noto_sans_regular)
        val Montserrat = Font(R.raw.montserrat_regular)
    }
}
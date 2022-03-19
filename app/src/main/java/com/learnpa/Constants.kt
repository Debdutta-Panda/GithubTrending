package com.learnpa

import androidx.compose.ui.text.font.Font

object Constants {

    const val BASE_URL = "https://api.coinpaprika.com/"

    const val PARAM_ITEM_ID = "itemId"

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
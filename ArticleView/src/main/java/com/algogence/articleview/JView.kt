package com.algogence.articleview

import android.util.Size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class NumberPair(
    val comp1: Number,
    val comp2: Number
)
data class NumberTuple4(
    val comp1: Number,
    val comp2: Number,
    val comp3: Number,
    val comp4: Number
)

object JConst{
    const val startIndent = "startIndent"
    const val thickness = "thickness"
    const val divider = "divider"
    const val dark = "dark"
    const val light = "light"
    const val theme = "theme"
    const val language = "language"
    const val code = "code"
    const val onClick = "onClick"
    const val webpage = "webpage"
    const val game = "game"
    const val video = "video"
    const val description = "description"
    const val title = "title"
    const val audio = "audio"
    const val lottie = "lottie"
    const val latex = "latex"
    const val svg = "svg"
    const val thumbnail = "thumbnail"
    const val youtube = "youtube"
    const val lazyColumn = "lazyColumn"
    const val lazyItemScopeFillParentMaxHeight = "lazyItemScopeFillParentMaxHeight"
    const val lazyItemScopeFillParentMaxWidth = "lazyItemScopeFillParentMaxWidth"
    const val fraction = "fraction"
    const val lazyItemScopeFillParentMaxSize = "lazyItemScopeFillParentMaxSize"
    const val userScrollEnabled = "userScrollEnabled"
    const val reverseLayout = "reverseLayout"
    const val lazyRow = "lazyRow"
    const val disableSelection = "disableSelection"
    const val selectionContainer = "selectionContainer"
    const val blurRadius = "blurRadius"
    const val shadow = "shadow"
    const val richText = "richText"
    const val cursive = "cursive"
    const val monospace = "monospace"
    const val serif = "serif"
    const val sansSerif = "sansSerif"
    const val default = "default"
    const val fontFamily = "fontFamily"
    const val flowColumn = "flowColumn"
    const val lastLineMainAxisAlignment = "lastLineMainAxisAlignment"
    const val crossAxisSpacing = "crossAxisSpacing"
    const val crossAxisAlignment = "crossAxisAlignment"
    const val mainAxisSpacing = "mainAxisSpacing"
    const val mainAxisAlignment = "mainAxisAlignment"
    const val expand = "expand"
    const val mainAxisSize = "mainAxisSize"
    const val flowRow = "flowRow"
    const val maxLines = "maxLines"
    const val softWrap = "softWrap"
    const val visible = "visible"
    const val ellipsis = "ellipsis"
    const val clip = "clip"
    const val textOverflow = "textOverflow"
    const val lineHeight = "lineHeight"
    const val textAlign = "textAlign"
    const val textDecoration = "textDecoration"
    const val letterSpacing = "letterSpacing"
    const val fontWeight = "fontWeight"
    const val normal = "normal"
    const val italic = "italic"
    const val fontStyle = "fontStyle"
    const val fontSize = "fontSize"
    const val tint = "tint"
    const val icon = "icon"
    const val contentAlignment = "contentAlignment"
    const val box = "box"
    const val stroke = "stroke"
    const val color = "color"
    const val elevation = "elevation"
    const val topStart = "topStart"
    const val topEnd = "topEnd"
    const val bottomEnd = "bottomEnd"
    const val bottomStart = "bottomStart"
    const val empty = ""
    const val backgroundColor = "backgroundColor"
    const val contentColor = "contentColor"
    const val type = "type"
    const val surface = "surface"
    const val text = "text"
    const val image = "image"
    const val row = "row"
    const val card = "card"
    const val column = "column"
    const val verticalArrangement = "verticalArrangement"
    const val verticalAlignment = "verticalAlignment"
    const val horizontalArrangement = "horizontalArrangement"
    const val horizontalAlignment = "horizontalAlignment"
    const val topstart = "topstart"
    const val topcenter = "topcenter"
    const val topend = "topend"
    const val centerstart = "centerstart"
    const val center = "center"
    const val justify = "justify"
    const val centerend = "centerend"
    const val bottomstart = "bottomstart"
    const val bottomcenter = "bottomcenter"
    const val bottomend = "bottomend"
    const val value = "value"
    const val name = "name"
    const val children = "children"
    const val modifiers = "modifiers"
    const val width = "width"
    const val boxScopeAlign = "boxScopeAlign"
    const val columnScopeAlign = "columnScopeAlign"
    const val rowScopeAlign = "rowScopeAlign"
    const val columnScopeWeight = "columnScopeWeight"
    const val rowScopeWeight = "rowScopeWeight"
    const val height = "height"
    const val background = "background"
    const val size = "size"
    const val widthHeight = "widthHeight"
    const val requiredWidthHeight = "requiredWidthHeight"
    const val widthIn = "widthIn"
    const val heightIn = "heightIn"
    const val sizeIn = "sizeIn"
    const val min = "min"
    const val max = "max"
    const val minWidth = "minWidth"
    const val maxWidth = "maxWidth"
    const val minHeight = "minHeight"
    const val maxHeight = "maxHeight"
    const val requiredWidth = "requiredWidth"
    const val requiredWidthIn = "requiredWidthIn"
    const val requiredHeightIn = "requiredHeightIn"
    const val requiredHeight = "requiredHeight"
    const val requiredSize = "requiredSize"
    const val requiredSizeIn = "requiredSizeIn"
    const val fillMaxWidth = "fillMaxWidth"
    const val fillMaxHeight = "fillMaxHeight"
    const val fillMaxSize = "fillMaxSize"
    const val wrapContentWidth = "wrapContentWidth"
    const val wrapContentHeight = "wrapContentHeight"
    const val wrapContentSize = "wrapContentSize"
    const val defaultMinSize = "defaultMinSize"
    const val padding = "padding"
    const val paddingSymmetric = "paddingSymmetric"
    const val horizontal = "horizontal"
    const val vertical = "vertical"
    const val start = "start"
    const val end = "end"
    const val spaceAround = "spaceAround"
    const val spaceBetween = "spaceBetween"
    const val spaceEvenly = "spaceEvenly"
    const val left = "left"
    const val top = "top"
    const val right = "right"
    const val bottom = "bottom"
    const val paddingAll = "paddingAll"
    const val paddingAbsolute = "paddingAbsolute"
    const val offset = "offset"
    const val absoluteOffset = "absoluteOffset"
    const val x = "x"
    const val y = "y"
    const val url = "url"
    const val shape = "shape"
    const val contentScale = "contentScale"
    const val crop = "crop"
    const val fit = "fit"
    const val fillHeight = "fillHeight"
    const val fillWidth = "fillWidth"
    const val inside = "inside"
    const val fillBounds = "fillBounds"
    const val roundedCornerShape = "roundedCornerShape"
    const val circleShape = "circleShape"
    const val roundedCornersShape = "roundedCornersShape"
    const val border = "border"
}
val J.viewType: String
get(){
    return get(JConst.type)?.asString()?:JConst.empty
}
val J.viewUrl: String
get(){
    return get(JConst.url)?.asString()?:JConst.empty
}
val J.viewBackgroundColor: Color
get(){
    return Color.parse(get(JConst.backgroundColor)?.asString()?:JConst.empty)
}
val J.viewContentColor: Color
get(){
    return Color.parse(get(JConst.contentColor)?.asString()?:JConst.empty)
}
val J.viewElevation: Number
get(){
    return get(JConst.elevation)?.asNumber()?:0
}
val J.viewShape: J?
get(){
    return get(JConst.shape)
}
///////////////////////////////////////////
val J.viewHorizontalAlignment: String
get(){
    return get(JConst.horizontalAlignment)?.asString()?:JConst.empty
}
val J.viewVerticalArrangement: String
get(){
    return get(JConst.verticalArrangement)?.asString()?:JConst.empty
}
val J.viewVerticalAlignment: String
get(){
    return get(JConst.verticalAlignment)?.asString()?:JConst.empty
}
val J.viewHorizontalArrangement: String
get(){
    return get(JConst.horizontalArrangement)?.asString()?:JConst.empty
}
////////////////////////////////////////
val J.viewContentScale: String
get(){
    return get(JConst.contentScale)?.asString()?:JConst.empty
}
val J.viewName: String
get(){
    return get(JConst.name)?.asString()?:JConst.empty
}
val J.viewValueWidthHeight: NumberPair
get(){
    val value = get(JConst.value)
    val c1 = value?.get(JConst.width)?.asNumber()?:0
    val c2 = value?.get(JConst.height)?.asNumber()?:0
    return NumberPair(c1,c2)
}
val J.viewValueXY: NumberPair
get(){
    val value = get(JConst.value)
    val c1 = value?.get(JConst.x)?.asNumber()?:0
    val c2 = value?.get(JConst.y)?.asNumber()?:0
    return NumberPair(c1,c2)
}
val J.viewValueSymmetric: NumberPair
get(){
    val value = get(JConst.value)
    val c1 = value?.get(JConst.horizontal)?.asNumber()?:0
    val c2 = value?.get(JConst.vertical)?.asNumber()?:0
    return NumberPair(c1,c2)
}
val J.viewValueMinWidthHeight: NumberPair
get(){
    val value = get(JConst.value)
    val c1 = value?.get(JConst.minWidth)?.asNumber()?:0
    val c2 = value?.get(JConst.minHeight)?.asNumber()?:0
    return NumberPair(c1,c2)
}
val J.viewValueSizeLimit: NumberTuple4
    get(){
        val value = get(JConst.value)
        val c1 = value?.get(JConst.minWidth)?.asNumber()?:0
        val c2 = value?.get(JConst.minHeight)?.asNumber()?:0
        val c3 = value?.get(JConst.maxWidth)?.asNumber()?:0
        val c4 = value?.get(JConst.maxHeight)?.asNumber()?:0
        return NumberTuple4(c1,c2,c3,c4)
    }
val J.viewValueCorners: NumberTuple4
    get(){
        val value = get(JConst.value)
        val c1 = value?.get(JConst.topStart)?.asNumber()?:0
        val c2 = value?.get(JConst.topEnd)?.asNumber()?:0
        val c3 = value?.get(JConst.bottomEnd)?.asNumber()?:0
        val c4 = value?.get(JConst.bottomStart)?.asNumber()?:0
        return NumberTuple4(c1,c2,c3,c4)
    }
val J.viewValueSides: NumberTuple4
    get(){
        val value = get(JConst.value)
        val c1 = value?.get(JConst.start)?.asNumber()?:0
        val c2 = value?.get(JConst.top)?.asNumber()?:0
        val c3 = value?.get(JConst.end)?.asNumber()?:0
        val c4 = value?.get(JConst.bottom)?.asNumber()?:0
        return NumberTuple4(c1,c2,c3,c4)
    }
val J.viewValueSidesAbsolute: NumberTuple4
    get(){
        val value = get(JConst.value)
        val c1 = value?.get(JConst.left)?.asNumber()?:0
        val c2 = value?.get(JConst.top)?.asNumber()?:0
        val c3 = value?.get(JConst.right)?.asNumber()?:0
        val c4 = value?.get(JConst.bottom)?.asNumber()?:0
        return NumberTuple4(c1,c2,c3,c4)
    }
val J.viewValueMinMax: NumberPair
    get(){
        val value = get(JConst.value)
        val c1 = value?.get(JConst.min)?.asNumber()?:0
        val c2 = value?.get(JConst.max)?.asNumber()?:0
        return NumberPair(c1,c2)
    }

val J.viewValue: String
get(){
    return get(JConst.value)?.asString()?:""
}
val J.viewValueJ: J?
get(){
    return get(JConst.value)
}

val J.viewModifiers: J?
get(){
    return get(JConst.modifiers)
}
val J.viewValueNumber: Number
get(){
    val v = get(JConst.value)
    return when(v?.type){
        JBase.Type.FLOAT -> v.asFloat() ?:0f
        JBase.Type.INTEGER -> v.asInt() ?:0
        JBase.Type.LONG -> v.asLong() ?:0L
        JBase.Type.DOUBLE -> v.asDouble() ?:0.0
        else -> 0
    }
}

@Composable
fun J.forEachChildView(block: @Composable (J)->Unit){
    val children = this[JConst.children]?.asArray()?.children
    val count = children?.size?:0
    for(i in 0 until count){
        val child = children?.get(i)
        if(child!=null){
            block(child)
        }
    }
}

val J.children: List<J>
get(){
    return (this[JConst.children]?.asArray()?.children)?: emptyList()
}

@Composable
fun J.forEachModifier(block: (J)-> Modifier?): Modifier{
    val children = this[JConst.modifiers]?.asArray()?.children
    val count = children?.size?:0
    var e = Modifier
    var r = e.then(e)
    for(i in 0 until count){
        val child = children?.get(i)
        if(child!=null){
            val m = block(child)
            if(m!=null){
                r = r.then(m)
            }
        }
    }
    return r
}

val Number.dp: Dp
get(){
    return Dp(value = this.toFloat())
}

fun Color.Companion.parse(colorString: String): Color =
    try {
        Color(color = android.graphics.Color.parseColor(colorString))
    } catch (e: Exception) {
        Color.Unspecified
    }
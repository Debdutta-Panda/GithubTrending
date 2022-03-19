package com.algogence.articleview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.algogence.articleview.svg.SVG


class SvgView : View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val svg: SVG = SVG.getFromString("")

// Create a canvas to draw onto

// Create a canvas to draw onto
        if (svg.getDocumentWidth() != -1f) {
            val newBM = Bitmap.createBitmap(
                Math.ceil(svg.getDocumentWidth().toDouble()).toInt(),
                Math.ceil(svg.getDocumentHeight().toDouble()).toInt(),
                Bitmap.Config.ARGB_8888
            )
            val bmcanvas = Canvas(newBM)

            // Clear background to white
            bmcanvas.drawRGB(255, 255, 255)

            // Render our document onto our canvas
            svg.renderToCanvas(bmcanvas)
        }
    }
}
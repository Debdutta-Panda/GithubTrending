package com.algogence.articleview

import android.content.Context
import android.graphics.*
import android.media.audiofx.Visualizer
import android.util.AttributeSet
import android.view.View
import kotlin.math.abs

class AudioVisualizer : View {
    enum class VisualizerMode{
        BAR,
        LINE,
        CIRCLE,
        BLAZING,
        CIRCLE_BAR,
        LINE_BAR,
        SQUARE
    }
    private var middleLine = Paint()
    private val circlePaint: Paint = Paint()
    private var radius = -1
    private var shader = android.graphics.LinearGradient(0f,
    0f,
    0f,
    height.toFloat(),
    android.graphics.Color.BLUE,
    android.graphics.Color.GREEN,
    Shader.TileMode.MIRROR /*or REPEAT*/)
    private val radiusMultiplier = 1f
    var mode = VisualizerMode.LINE
    var bytes: ByteArray? = null
    set(value){
        field = value
        invalidate()
    }
    protected var paint = Paint().apply {
        color = Color.GRAY
        isAntiAlias = true
    }
    protected var visualizer: Visualizer? = null
    protected var color = Color.BLUE

    private var points: FloatArray? = null
    private val rect = Rect()
    private val strokeWidth = 0.005f

    private val density = 50f
    private val gap = 12

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
        when(mode){
            VisualizerMode.LINE -> {
                if (bytes != null&& bytes!!.isNotEmpty()) {
                    if (points == null || points!!.size < bytes!!.size * 4) {
                        points = FloatArray(bytes!!.size * 4)
                    }
                    paint.strokeWidth = height * strokeWidth
                    rect.set(0, 0, width, height)
                    for (i in 0 until bytes!!.size - 1) {
                        points!![i * 4] = (rect.width() * i / (bytes!!.size - 1)).toFloat()
                        points!![i * 4 + 1] = ((rect.height() / 2
                                + (bytes!!.get(i) + 128).toByte() * (rect.height() / 3) / 128)).toFloat()
                        points!![i * 4 + 2] = (rect.width() * (i + 1) / (bytes!!.size - 1)).toFloat()
                        points!![i * 4 + 3] = ((rect.height() / 2
                                + (bytes!![i + 1] + 128).toByte() * (rect.height() / 3) / 128)).toFloat()
                    }
                    canvas!!.drawLines(points!!, paint)
                }
            }
            VisualizerMode.BAR -> {
                if (bytes != null&& bytes!!.isNotEmpty()) {
                    val barWidth: Float = width / density
                    val div: Float = bytes!!.size / density
                    paint.strokeWidth = barWidth - gap
                    var i = 0
                    while (i < density) {
                        val bytePosition = Math.ceil((i * div).toDouble()).toInt()
                        val top = height +
                                (abs((bytes!![bytePosition]).toInt()) + 128).toByte() * height / 128
                        val barX = i * barWidth + barWidth / 2
                        canvas!!.drawLine(barX, height.toFloat(), barX, top.toFloat(), paint)
                        i++
                    }
                }
            }
            VisualizerMode.CIRCLE -> {
                if (bytes != null&& bytes!!.isNotEmpty()) {
                    paint.strokeWidth = height * strokeWidth
                    if (points == null || points!!.size < bytes!!.size * 4) {
                        points = FloatArray(bytes!!.size * 4)
                    }
                    var angle = 0.0
                    var i = 0
                    while (i < 360) {
                        points!![i * 4] = (width / 2
                                + (Math.abs(bytes!![i * 2].toInt())
                                * radiusMultiplier
                                * Math.cos(Math.toRadians(angle)))).toFloat()
                        points!![i * 4 + 1] = (height / 2
                                + (Math.abs(bytes!![i * 2].toInt())
                                * radiusMultiplier
                                * Math.sin(Math.toRadians(angle)))).toFloat()
                        points!![i * 4 + 2] = (width / 2
                                + (Math.abs(bytes!![i * 2 + 1].toInt())
                                * radiusMultiplier
                                * Math.cos(Math.toRadians(angle + 1)))).toFloat()
                        points!![i * 4 + 3] = (height / 2
                                + (Math.abs(bytes!![i * 2 + 1].toInt())
                                * radiusMultiplier
                                * Math.sin(Math.toRadians(angle + 1)))).toFloat()
                        i++
                        angle++
                    }
                    canvas!!.drawLines(points!!, paint)
                }
            }
            VisualizerMode.BLAZING -> {
                if (bytes != null&& bytes!!.isNotEmpty()) {
                    paint.shader = shader
                    var i = 0
                    var k = 0
                    while (i < bytes!!.size - 1 && k < bytes!!.size) {
                        val top = height +
                                (Math.abs((bytes!![k]) + 128)).toByte() * height / 128
                        canvas!!.drawLine(
                            i.toFloat(),
                            height.toFloat(),
                            i.toFloat(),
                            top.toFloat(),
                            paint
                        )
                        i++
                        k++
                    }
                    super.onDraw(canvas)
                }
            }
            VisualizerMode.CIRCLE_BAR -> {
                if (radius == -1) {
                    radius = if (height < width) height else width
                    radius = (radius * 0.65 / 2).toInt()
                    val circumference: Double = 2 * Math.PI * radius
                    paint.strokeWidth = (circumference / 120).toFloat()
                    circlePaint.setStyle(Paint.Style.STROKE)
                    circlePaint.setStrokeWidth(4f)
                }
                circlePaint.setColor(color)
                canvas!!.drawCircle(width / 2f, height / 2f, radius.toFloat(), circlePaint)
                if (bytes != null&& bytes!!.isNotEmpty()) {
                    if (points == null || points!!.size < bytes!!.size * 4) {
                        points = FloatArray(bytes!!.size * 4)
                    }
                    var angle = 0.0
                    var i = 0
                    while (i < 120) {
                        val x = Math.ceil(i * 8.5).toInt()
                        val t = (-Math.abs((bytes!![x]) + 128)).toByte() * (height / 4) / 128
                        points!![i * 4] = (width / 2
                                + radius
                                * Math.cos(Math.toRadians(angle))).toFloat()
                        points!![i * 4 + 1] = (height / 2
                                + radius
                                * Math.sin(Math.toRadians(angle))).toFloat()
                        points!![i * 4 + 2] = (width / 2
                                + (radius + t)
                                * Math.cos(Math.toRadians(angle))).toFloat()
                        points!![i * 4 + 3] = (height / 2
                                + (radius + t)
                                * Math.sin(Math.toRadians(angle))).toFloat()
                        i++
                        angle += 3.0
                    }
                    canvas.drawLines(points!!, paint)
                }
            }
            VisualizerMode.LINE_BAR -> {
                if (middleLine.getColor() != Color.BLUE) {
                    middleLine.setColor(color)
                }
                if (bytes != null&& bytes!!.isNotEmpty()) {
                    val barWidth = width / density
                    val div: Float = bytes!!.size / density
                    canvas!!.drawLine(
                        0f, (height / 2).toFloat(),
                        width.toFloat(), (height / 2).toFloat(), middleLine
                    )
                    paint.strokeWidth = barWidth - gap
                    var i = 0
                    while (i < density) {
                        val bytePosition = Math.ceil((i * div).toDouble()).toInt()
                        val top: Int = (height / 2
                                + (128 - Math.abs(bytes!![bytePosition].toInt()))
                                * (height / 2) / 128)
                        val bottom: Int = (height / 2
                                - (128 - Math.abs(bytes!![bytePosition].toInt()))
                                * (height / 2) / 128)
                        val barX = i * barWidth + barWidth / 2
                        canvas.drawLine(barX, bottom.toFloat(), barX, (height / 2).toFloat(), paint)
                        canvas.drawLine(barX, top.toFloat(), barX, (height / 2).toFloat(), paint)
                        i++
                    }
                }
            }
            VisualizerMode.SQUARE ->{
                if (bytes != null&& bytes!!.isNotEmpty()) {
                    val barWidth = width / density
                    val div: Float = bytes!!.size / density
                    paint.strokeWidth = barWidth - gap
                    var i = 0
                    while (i < density) {
                        var count = 0
                        val bytePosition = Math.ceil((i * div).toDouble()).toInt()
                        val top =
                            height + (Math.abs(bytes!![bytePosition].toInt()) + 128).toByte() * height / 128
                        val col = Math.abs(height - top)
                        var j = 0
                        while (j < col + 1) {
                            val barX = i * barWidth + barWidth / 2
                            val y1 = height - (barWidth + gap / 2f) * count
                            val y2 = height - (barWidth - gap / 2f + (barWidth + gap / 2f) * count)
                            canvas!!.drawLine(barX, y1, barX, y2, paint)
                            count++
                            j += barWidth.toInt()
                        }
                        i++
                    }
                    super.onDraw(canvas)
                }
            }
        }

        super.onDraw(canvas)
    }

    fun cycleMode(){
        bytes = null
        points = null
        paint.shader = null
        nextMode()
    }

    private fun nextMode() {
        mode = mode.next()
    }
    inline fun <reified T: Enum<T>> T.next(): T {
        val values = enumValues<T>()
        val nextOrdinal = (ordinal + 1) % values.size
        return values[nextOrdinal]
    }
}
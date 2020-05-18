package com.wan.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

class BorderSpan(
    private val borderColor: Int,
    private val borderRadius: Float,
    private val borderWidth: Float,
    private val textColor: Int,
    private val textSize: Float,
    private val horizontalPadding: Float,
    private val verticalPadding: Float
) : ReplacementSpan() {
    private var mSize = 0

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        paint.textSize = textSize
        mSize =
            (paint.measureText(text, start, end) + 2 * borderRadius + 2 * horizontalPadding).toInt()
        return mSize
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        paint.color = borderColor
        paint.isAntiAlias = true
        val style = paint.style
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
        paint.textSize = textSize

        val fontMetrics = paint.fontMetrics
        val offSet: Float = (fontMetrics.descent + fontMetrics.ascent) / 2
        val rectOffset = (fontMetrics.bottom - fontMetrics.top) / 2 + verticalPadding
        val verticalCenter = (top + bottom) / 2

        val oval = RectF(
            x,
            verticalCenter - rectOffset,
            x + mSize,
            verticalCenter + rectOffset
        )
        canvas.drawRoundRect(oval, borderRadius, borderRadius, paint)

        // restore paint
        paint.color = textColor
        paint.style = style
        canvas.drawText(
            text ?: "",
            start,
            end,
            x + borderRadius + horizontalPadding,
            verticalCenter - offSet,
            paint
        )
    }

}
package com.rmunidasa.jobsearch.ui.jobsearch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * Creates Cubic Bezier based shape view to be used as the
 * background of job earnings label
 *
 * Bezier curve calculation is based on following online tool
 * https://www.desmos.com/calculator/ebdtbxgbq0
 */
class BezierCurvedView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()
    private val path = Path()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        paint.strokeWidth = 2.0F

        val height = height.toFloat()
        path.moveTo(0F, height)
        // draw cubic bezier
        path.cubicTo(10.6F, height - 0.5F, 20.2F, -1.3F, 30F, 0F)
        path.lineTo(width.toFloat(), 0F)
        path.lineTo(width.toFloat(), height)
        // draw the outer path
        path.lineTo(0F, height)

        canvas?.drawPath(path, paint)
    }
}
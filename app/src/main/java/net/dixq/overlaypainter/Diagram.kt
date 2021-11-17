package net.dixq.overlaypainter

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent

open class Diagram(paint: Paint) {
    val _paint:Paint = Paint()
    init {
        _paint.isAntiAlias = paint.isAntiAlias
        _paint.style = paint.style
        _paint.strokeWidth = paint.strokeWidth
        _paint.color = paint.color
    }
    open fun onDraw(canvas: Canvas) {
    }

    open fun onTouchEvent(event: MotionEvent): Diagram? {
        return null
    }
}
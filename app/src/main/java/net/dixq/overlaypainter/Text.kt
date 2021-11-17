package net.dixq.overlaypainter

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.view.MotionEvent

class Text(paint: Paint) : Diagram(paint) {

    var _now:PointF? = null

    init {
        _paint.textSize = 100F
    }

    override fun onDraw(canvas: Canvas){
        Lg.e("onDraw")
        if(_now!=null){
            canvas.drawText("Android!!",_now!!.x, _now!!.y, _paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Diagram? {
        Lg.e("onTouchEvent")
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                _now = PointF(event.x, event.y)
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                _now = PointF(event.x, event.y)
                return this
            }

        }
        return null
    }
}
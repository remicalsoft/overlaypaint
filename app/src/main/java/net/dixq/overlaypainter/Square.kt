package net.dixq.overlaypainter

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.view.MotionEvent

class Square(paint: Paint) : Diagram(paint) {

    var _start:PointF? = null
    var _now:PointF? = null

    override fun onDraw(canvas: Canvas){
        Lg.e("onDraw")
        if(_start!=null && _now!=null){
            var x = _start!!.x
            var y = _start!!.y
            var w = _now!!.x - _start!!.x
            var h = _now!!.y - _start!!.y
            if(w<0){
                x = _now!!.x
                w = -w
            }
            if(h<0){
                y = _now!!.y
                h = -h
            }
            var r = w
            if(w<h){
                r = h
            }
            canvas.drawRect(x, y, x+w, y+h, _paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Diagram? {
        Lg.e("onTouchEvent")
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                _start = PointF(event.x, event.y)
            }
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
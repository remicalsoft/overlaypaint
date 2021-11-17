package net.dixq.overlaypainter

import android.content.Context
import android.graphics.*
import android.view.MotionEvent

class Image(context: Context, paint: Paint) : Diagram(paint) {

    var _now:PointF? = null
    val _bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.android)

    override fun onDraw(canvas: Canvas){
        Lg.e("onDraw")
        if(_now!=null){
            canvas.drawBitmap(_bitmap, _now!!.x, _now!!.y, _paint)
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
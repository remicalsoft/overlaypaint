package net.dixq.overlaypainter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.icu.text.Transliterator
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.*

class PaintView : View {

    var _color = Color.RED
    var _list = LinkedList<FloatArray>()
    var _drawingPos = FloatArray(0)
    val _paint = Paint()
    var _isPaintable = true

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    fun setColor(color:Int){
        _paint.color = color
        invalidate()
    }

    fun change(){
        _isPaintable = !_isPaintable
        invalidate()
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        _paint.color = _color
        _paint.strokeWidth = 10F
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.e("a", "onDraw")
        canvas?.drawColor(0, PorterDuff.Mode.CLEAR)
        if(_list.isNotEmpty()) {
            Log.e("a", "notEmpty")
            for(pos in _list) {
                Log.e("a", "drawLine")
                canvas?.drawLines(pos, _paint)
                for(i in 0 until pos.size-2 step 2){
                    Log.e("a", pos[i].toString()+","+pos[i+1])
                }
            }
        }
        if(_drawingPos.size != 0){
            canvas?.drawLines(_drawingPos, _paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        if(!_isPaintable){
            val ret = super.onTouchEvent(event)
            Log.e("a", "ret : "+ret)
            return ret
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                Log.e("a", "onTouchEvent down move")
                if(_drawingPos.size>2){
                    _drawingPos += _drawingPos[_drawingPos.size-2]
                    _drawingPos += _drawingPos[_drawingPos.size-2]
                }
                _drawingPos += event.x
                _drawingPos += event.y
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                Log.e("a", "onTouchEvent up cancel")
                _drawingPos += event.x
                _drawingPos += event.y
                _list.add(_drawingPos)
                _drawingPos = FloatArray(0)
                invalidate()
            }
        }
        return _isPaintable
    }

}
package net.dixq.overlaypainter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.collections.ArrayList

class PaintView : View {

    data class PathInfo(val path:Path?, val paint:Paint, val diagram: Diagram?)

    val PointerMax = 10

    var _color = Color.rgb(255,70,70)
    var _fixedList = ArrayList<PathInfo>()
    var _redoList = LinkedList<PathInfo>()
    val _paint = Paint()
    var _prePoints = Array(PointerMax){ PointF() }
    var _paths = Array(PointerMax){ Path() }
    var _diagram:Diagram? = null

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
        _diagram = null
        _paint.color = color
        invalidate()
    }

    fun undo() {
        if(_fixedList.isNotEmpty()) {
            _redoList.add(_fixedList.last())
            _fixedList.removeLast()
            invalidate()
        }
    }

    fun redo() {
        if(_redoList.isNotEmpty()){
            _fixedList.add(_redoList.last())
            _redoList.removeLast()
            invalidate()
        }
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        _paint.color = _color
        _paint.strokeWidth = 10F
        _paint.style = Paint.Style.STROKE
        _paint.isAntiAlias = true;
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        for(p in _fixedList){
            if(p.path!=null) {
                canvas?.drawPath(p.path, p.paint)
            }
            if(p.diagram!=null){
                p.diagram.onDraw(canvas!!)
            }
        }
        for(p in _paths){
            if(p.isEmpty.not()){
                canvas?.drawPath(p, _paint)
            }
        }
        if(_diagram!=null){
            _diagram!!.onDraw(canvas!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        if(_diagram!=null){
            val d = _diagram!!.onTouchEvent(event)
            if(d!=null){
                addToFixedList(createPathInfo(null, _paint, d))
                _diagram = createSameDiagram(_diagram!!)
            }
            invalidate()
            return true
        }

        val index = event.action shr 8
        val id = event.getPointerId(index)
        if(id >= PointerMax) return false
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                for(i in 0 until event.pointerCount) {
                    val id = event.getPointerId(i)
                    _paths[id].moveTo(event.getX(i), event.getY(i))
                    _prePoints[id] = PointF(event.getX(i), event.getY(i))
                }
            }
            MotionEvent.ACTION_MOVE -> {
                add(event)
                invalidate()
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_CANCEL -> {
                add(event)
                invalidate()
                _prePoints[id] = PointF()
                addToFixedList(createPathInfo(_paths[id], _paint, null))
                _paths[id] = Path()
            }
        }
        return true
    }

    private fun addToFixedList(inf:PathInfo){
        _fixedList.add(inf)
        _redoList.clear()
    }

    private fun createPathInfo(path: Path?, paint: Paint, diagram:Diagram?): PathInfo {
        val p = Paint()
        p.color = paint.color
        p.strokeWidth = paint.strokeWidth
        p.style = paint.style
        p.isAntiAlias = paint.isAntiAlias
        return PathInfo(path, p, diagram)
    }

    private fun add(event: MotionEvent){
        val history: Int = event.historySize
        Lg.e("count : "+event.pointerCount);
        for(i in 0 until event.pointerCount) {
            val id = event.getPointerId(i)
            for (h in 0 until history) {
                _paths[id].quadTo(_prePoints[id].x, _prePoints[id].y, event.getHistoricalX(i, h), event.getHistoricalY(i, h))
                _prePoints[id] = PointF(event.getHistoricalX(i, h), event.getHistoricalY(i, h))
            }
            _paths[id].quadTo(_prePoints[id].x, _prePoints[id].y, event.getX(i), event.getY(i))
            _prePoints[id] = PointF(event.getX(i), event.getY(i))
        }
    }



    fun circle() {
        _diagram = Circle(_paint)
    }

    fun square() {
        _diagram = Square(_paint)
    }

    fun text() {
        _diagram = Text(_paint)
    }

    fun img() {
        _diagram = Image(context, _paint)
    }

    fun createSameDiagram(diagram: Diagram): Diagram {
        if(diagram is Circle){
            return Circle(_paint)
        }
        if(diagram is Square){
            return Square(_paint)
        }
        if(diagram is Text){
            return Text(_paint)
        }
        if(diagram is Image){
            return Image(context, _paint)
        }
        return Circle(_paint)
    }

}
package com.example.customview.customview

import android.content.Context
import android.graphics.Color
import android.graphics.DiscretePathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import com.example.customview.model.Bubble
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyBubbleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SurfaceView(context, attrs) {

    private val colors = arrayOf(Color.RED, Color.YELLOW,Color.GREEN,Color.WHITE,Color.BLUE,Color.GRAY,Color.MAGENTA)
    private val bubbleList = mutableListOf<Bubble>()
    private var segmentLength = 0f
    private var deviation = 0f
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (true){
                if (holder.surface.isValid){
                    val canvas = holder.lockCanvas()
                    canvas.drawColor(Color.BLACK)
                    bubbleList.toList().filter { it.radius < 1000  } .forEach {
                        paint.color = it.color
                        paint.pathEffect = DiscretePathEffect(segmentLength,deviation)
                        canvas.drawCircle(it.xPont,it.yPoint,it.radius,paint)
                        it.radius += 10f
                    }
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val bubble = Bubble(event?.x?:0f,event?.y?:0f,1f,colors.random())
        bubbleList.add(bubble)
        if (bubbleList.size > 30){
            bubbleList.removeAt(0)
        }
        performClick()
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    fun setPathEffect(seg:Float,dev:Float){
        this.segmentLength = seg
        this.deviation = dev
    }
}
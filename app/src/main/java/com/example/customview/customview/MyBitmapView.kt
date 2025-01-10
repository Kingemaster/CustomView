package com.example.customview.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.customview.R
import kotlin.random.Random
import kotlin.random.nextUInt

class MyBitmapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var randomX = 0f
    private var randomY = 0f
    private val bitmap = ContextCompat.getDrawable(context, R.drawable.baseline_face_24)?.toBitmap(300,300)!!
    private val path = Path()
    private val pathPaint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context,R.color.purple_700)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawBitmap(bitmap,randomX,randomY,null)
            drawPath(path,pathPaint)
            //drawPath(path2,pathPaint)
            //drawPath(path1,pathPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            when(action){
                MotionEvent.ACTION_DOWN -> {
                    randomPosition()
                    path.reset()
                    path.addRect(0f,0f,width.toFloat(),height.toFloat(),Path.Direction.CW)
                    path.addCircle(x,y,200f,Path.Direction.CCW)
                }
                MotionEvent.ACTION_MOVE -> {
                    path.reset()
                    path.addRect(0f,0f,width.toFloat(),height.toFloat(),Path.Direction.CW)
                    path.addCircle(x,y,200f,Path.Direction.CCW)
                }
                MotionEvent.ACTION_UP ->{
                    path.reset()
                }
            }
            invalidate()
        }
        performClick()
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private fun randomPosition(){
        randomX = Random.nextInt(width - 300).toFloat()
        randomY = Random.nextInt(height -300).toFloat()
    }

}
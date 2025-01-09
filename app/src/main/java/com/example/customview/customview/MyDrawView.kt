package com.example.customview.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.compose.ui.text.TextStyle
import androidx.core.content.ContextCompat
import androidx.core.graphics.TypefaceCompat
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.customview.R
import kotlinx.coroutines.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

class MyDrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr),LifecycleObserver {

    private var mAngle = 30f
    private var mRadius = 0f
    private var mWidth = 0f
    private var mHeight = 0f
    private var mRandomX = 0f
    private var mRandomY = 0f
    private var mCoroutineJob:Job? = null
    private var mCoroutineJob1:Job? = null

    private val solidLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.white)
        strokeWidth = 5f
    }
    private val textPaint = Paint().apply {
        textSize = 30f
        color = ContextCompat.getColor(context, R.color.white)
        typeface = Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)
    }
    private val dashedCirclePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.yellow)
        pathEffect = DashPathEffect(floatArrayOf(10f,10f),0f)
        strokeWidth = 5f
    }
    private val dashedLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.purple_200)
        pathEffect = DashPathEffect(floatArrayOf(10f,10f),0f)
        strokeWidth = 5f
    }
    private val vectorPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.teal_200)
        strokeWidth = 5f
    }
    private val fillCirclePaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = ContextCompat.getColor(context, R.color.white)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.mWidth = w.toFloat()
        this.mHeight = h.toFloat()
        this.mRadius = min( mWidth / 2, mHeight / 4) - 30
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawAxis(this)
            drawLabel(this)
            drawDashedCircle(this)
            drawVector(this)
            drawProjections(this)
        }
    }

    private fun drawAxis(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint)
            drawLine(0f, -mHeight / 2, 0f, mHeight / 2, solidLinePaint)
        }
        canvas.withTranslation(mWidth / 2, mHeight * 3 / 4) {
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint)
        }
    }
    private fun drawLabel(canvas: Canvas){
        canvas.withTranslation(mWidth / 4*3 - 50,mHeight - 30){
            drawText("指数函数和矢量旋转",0f,0f,textPaint)
        }
    }

    private fun drawDashedCircle(canvas: Canvas){
        canvas.withTranslation(mWidth / 2, mHeight * 3 / 4) {
            canvas.drawCircle(0f,0f,mRadius,dashedCirclePaint)
        }
    }

    private fun drawVector(canvas: Canvas){
        canvas.withTranslation(mWidth / 2, mHeight * 3 / 4){
            withRotation(-mAngle){
                drawLine(0f,0f,mRadius,0f,vectorPaint)
            }
        }
    }

    private fun drawProjections(canvas: Canvas){
        //绘制上方的投影点
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            drawCircle(mRadius * cos(mAngle.toRadians()),0f,20f,fillCirclePaint)
        }
        //绘制下方的投影点
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawCircle(mRadius * cos(mAngle.toRadians()),0f,20f,fillCirclePaint)
        }
        //绘制两个投影点中间的直线，一半是直线，一半是虚线
        //先将画布原点移动到圆与旋转的半径相交的点
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3){
            val x = mRadius * cos(mAngle.toRadians())
            val y = mRadius * sin(mAngle.toRadians())
            withTranslation(x,-y) {
                drawLine(0f,0f,0f,y,solidLinePaint)
                drawLine(0f,0f,0f,-(mHeight / 4 - y),dashedLinePaint)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startRotating(){
        mCoroutineJob = MainScope().launch {
            while (true){
                delay(100)
                mAngle +=5
                invalidate()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopRotating(){
        mCoroutineJob?.cancel()
    }

    private fun Float.toRadians() = Math.toRadians(this.toDouble()).toFloat()

}
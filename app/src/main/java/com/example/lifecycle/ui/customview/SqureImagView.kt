package com.example.lifecycle.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import com.example.lifecycle.R
import com.example.lifecycle.utils.SharedPrefModel
import com.ortiz.touchview.TouchImageView
import com.photo.utils.Constants



class SqureImagView (context: Context, attributeSet: AttributeSet): AppCompatImageView(context,attributeSet) {

    private var downX  = 0
    private var gprPaddingTop = 120f
    private var squareWidth = 50f
    private var squareHeight = 50f
    private var squarePaddingTop = gprPaddingTop + 24

    private var lastX  = 0
    private var lastY  = 0
    private var pointer  = false
    val paddingLeft = 80f
    var mMatrix : Matrix = Matrix()
    val midPaint = Paint()
    val bitmap =Bitmap.createBitmap(500,500,Bitmap.Config.ARGB_8888)
    var scaleTemp = 1f
    var scale= 1f
    var isScale = false
    //像素宽高
    var rectWidth = 1f
    var rectHeight = 2f
    val gprImageViewWidth = rectWidth*Constants.DefaultTraces
    val gprImageViewHeight = rectHeight*SharedPrefModel.samples
    init {
        val canvas = Canvas(bitmap)
        canvas.drawColor(context.getColor(R.color.lineColor))
        midPaint.color = Color.RED
        midPaint.strokeWidth = 10f
        midPaint.style = Paint.Style.STROKE
        this.setImageBitmap(bitmap)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(squareWidth.toInt(),squareHeight.toInt())
    }
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(true, 0, 0, width, height)
    }

    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.rawX.toInt()
        val y = event!!.rawY.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录触摸点坐标
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                // 计算偏移量
                var offsetX: Int = x - lastX
                var offsetY: Int = y - lastY

                if((left+offsetX)<(30+paddingLeft)){
                    offsetX = 0
                }
                if((right+offsetX)>(30+paddingLeft+gprImageViewWidth)){
                    offsetX = 0
                }

                if((top+offsetY)<(squarePaddingTop)){
                    offsetY = 0
                }
                if((bottom+offsetY)>(squarePaddingTop+gprImageViewHeight)){
                    offsetY = 0
                }
                layout(left + offsetX, top+offsetY , right + offsetX, bottom+offsetY)
                invalidate()
                //重新设置初始化坐标
                lastX = x
                lastY = y
            }
        }

        return true
    }




}




package com.example.lifecycle.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.example.lifecycle.R
import com.example.lifecycle.utils.SharedPrefModel
import com.photo.utils.Constants


class LineImagView ( context: Context,attributeSet: AttributeSet): AppCompatImageView(context,attributeSet) {

    private var lastX  = 0
    private var lastY  = 0
    val paddingLeft = 80f
    var mMatrix : Matrix = Matrix()
    val midPaint = Paint()
    var defaultX = 0f
    var onTranslate:((Int) -> Unit)? = null
    //像素宽高
    val rectWidth = 1f
    val gprImageViewWidth = rectWidth*Constants.DefaultTraces

    init {
        midPaint.color = Color.RED
        midPaint.strokeWidth = 10f
        midPaint.style = Paint.Style.STROKE
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode=MeasureSpec.getMode(widthMeasureSpec)
        val widthSize=MeasureSpec.getSize(widthMeasureSpec)

        val heightMode=MeasureSpec.getMode(heightMeasureSpec)
        val heightSize=MeasureSpec.getSize(heightMeasureSpec)

        var width=(getPaddingLeft()+getPaddingRight()+20f).toInt()
        var height=(getPaddingBottom()+getPaddingTop()+20f).toInt()

        if(widthMode==MeasureSpec.EXACTLY){
            //如果match_parent或者具体的值，直接赋值
            width=widthSize
        }
        //高度跟宽度处理方式一样
        if(heightMode==MeasureSpec.EXACTLY){
            height=heightSize
        }
        //保存测量宽度和测量高度
        setMeasuredDimension(width,height)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(true, 0, 0, width, height)
    }


    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas!!.drawColor(R.color.lineColor)
        mMatrix.preTranslate(5f,0f)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.rawX.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录触摸点坐标
                lastX = x
            }
            MotionEvent.ACTION_MOVE -> {
                // 计算偏移量
                var offsetX: Int = x - lastX
         
                if((left+offsetX)<(30+paddingLeft)){
                    offsetX = 0
                }
                if((right+offsetX)>(30+paddingLeft+gprImageViewWidth)){
                    offsetX = 0
                }
                layout(left + offsetX, top , right + offsetX, bottom)
                var trace = ((x-30-paddingLeft)/gprImageViewWidth * Constants.DefaultTraces).toInt()
                if (trace>=Constants.DefaultTraces){
                   trace = Constants.DefaultTraces-1
                }else if(trace<0){
                    trace = 0
                }
                SharedPrefModel.mMidLinePos = trace
                onTranslate?.invoke(trace)
                //重新设置初始化坐标
                lastX = x
            }
        }

        return true
    }



}
package com.example.lifecycle.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.example.lifecycle.R
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.utils.SharedPrefModel
import com.photo.utils.Constants


class VerticalLine (context: Context, attributeSet: AttributeSet): AppCompatImageView(context,attributeSet) {

    val dataInstance = GPRDataManager
    private var lastX  = 0
    var trace = 0
    val paddingLeft = 80f
    val gprPaddingTop = 120f
    val midPaint = Paint()
    var defaultX = 0f
    var onTranslate:((Int) -> Unit)? = null
    //像素宽高
    val rectWidth = 1f
    val rectHeight = dataInstance.gprHeight
    val gprImageViewWidth = rectWidth*dataInstance.defaultTraces
    val gprImageViewHeight = rectHeight*dataInstance.samples
    
    init {
        midPaint.color = context.getColor(R.color.white)
        midPaint.strokeWidth = 5f
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(true, 0, 0, width, height)
    }


    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.triangle)
        val mainBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        val mainCanvas = Canvas(mainBitmap)

        mainCanvas.drawBitmap(bitmap,6f,gprPaddingTop+gprImageViewHeight-10,midPaint)
        mainCanvas.drawLine(width/2f,gprPaddingTop,width/2f,gprPaddingTop+gprImageViewHeight,midPaint)
        canvas!!.drawBitmap(mainBitmap,0f,0f,midPaint)

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

                if((left+offsetX)<(30+paddingLeft-width/2)){
                    offsetX = 0
                }
                if((right+offsetX)>(30+paddingLeft+gprImageViewWidth+width/2)){
                    offsetX = 0
                }
                layout(left + offsetX, top , right + offsetX, bottom)
                trace = ((x-30-paddingLeft)/gprImageViewWidth * dataInstance.defaultTraces).toInt()
                if (trace>=dataInstance.defaultTraces){
                   trace = dataInstance.defaultTraces-1
                }else if(trace<0){
                    trace = 0
                }
                onTranslate?.invoke(trace)
                //重新设置初始化坐标
                lastX = x
            }
        }

        return true
    }



}
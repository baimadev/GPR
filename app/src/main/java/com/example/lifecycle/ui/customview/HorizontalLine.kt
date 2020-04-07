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


class HorizontalLine (context: Context, attributeSet: AttributeSet): AppCompatImageView(context,attributeSet) {

    val dataInstance = GPRDataManager
    var trace = 0
    private var lastY  = 0
    val gprPaddingLeft = 80f
    val gprPaddingTop = 120f
    val midPaint = Paint()
    var defaultX = 0f
    var onTranslate:((Int) -> Unit)? = null
    //像素宽高
    val rectWidth = 1f
    val rectHeight = 2f
    val gprImageViewWidth = rectWidth*dataInstance.defaultTraces
    val gprImageViewHeight = rectHeight*dataInstance.samples
    val leftStart = gprPaddingLeft
    val leftEnd = leftStart+gprImageViewWidth
    val topStart = 30+gprPaddingTop
    val topEnd = 30+gprPaddingTop+gprImageViewHeight
    var mLeft = 0
    var mRight = 0
    var mTop = 0
    var mBottom = 0
    var bitmap :Bitmap
    lateinit var mainBitmap :Bitmap
    init {
        midPaint.color = context.getColor(R.color.white)
        midPaint.strokeWidth = 5f
        bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.triangle_horizontal)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        trace = ((top-topStart+height/2)/gprImageViewHeight * dataInstance.samples).toInt()
        if (trace>=dataInstance.samples){
            trace = dataInstance.samples-1
        }else if(trace<0){
            trace = 0
        }
    }


    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mainBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        val mainCanvas = Canvas(mainBitmap)
        mainCanvas.drawBitmap(bitmap,leftEnd,8f,midPaint)
        mainCanvas.drawLine(leftStart,height/2f,leftEnd,height/2f,midPaint)
        canvas!!.drawBitmap(mainBitmap,0f,0f,midPaint)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val y = event!!.rawY.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录触摸点坐标
                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                // 计算偏移量
                var offsetY: Int = y - lastY
         
                if((top+offsetY)<(topStart-height/2)){
                    offsetY = 0
                }
                if((bottom+offsetY)>(topEnd+height/2)){
                    offsetY = 0
                }
                layout(left , top+offsetY , right , bottom+offsetY)
                //offsetTopAndBottom(offsetY)
                //scrollTo(0,offsetY)
                mLeft = left
                mRight = right
                mTop = top
                mBottom = bottom
                trace = ((top-topStart+height/2)/gprImageViewHeight * dataInstance.samples).toInt()
                if (trace>=dataInstance.samples){
                   trace = dataInstance.samples-1
                }else if(trace<0){
                    trace = 0
                }
                onTranslate?.invoke(trace)
                //重新设置初始化坐标
                lastY = y
            }
        }

        return true
    }



}
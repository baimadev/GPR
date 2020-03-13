package com.example.lifecycle.ui.customview

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import com.example.lifecycle.R
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.model.GPRDataMatrix
import com.example.lifecycle.utils.SharedPrefModel
import com.photo.utils.Constants
import kotlin.math.abs


class EnergyImageView (context: Context, attributeSet: AttributeSet): ImageView(context,attributeSet) {


    private val midLineWidth = 8
    var gprData:GPRDataMatrix? = null
    val midPaint = Paint()
    var defaultX = 0f
    var defaultY = 0f
    var trace = Constants.DefaultTraces/2
    var max = 0f
    var min = 0f
    lateinit var lastPos:Pair<Float,Float>

    init {
        gprData = GPRDataManager.matrixT
        midPaint.color = Color.RED
        midPaint.style = Paint.Style.FILL
        max = gprData!!.max
        max = gprData!!.min
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        defaultX = (height/SharedPrefModel.samples).toFloat()
        defaultY = width/abs(max - min)
        lastPos = width/2f to 0f
        canvas!!.drawColor(R.color.colorPrimary)
        canvas.drawLine(width/2f-(midLineWidth/2f),0f,width/2f-(midLineWidth/2f),height.toFloat(),midPaint)

        midPaint.color = R.color.back
        midPaint.style = Paint.Style.STROKE
        midPaint.strokeWidth = 5f
        midPaint.isAntiAlias = true

        gprData!!.matrix[trace].forEachIndexed { index, i ->
            val x = width/2f+ i*defaultY
            val y = (index+1) * defaultX
            val currentPos = x to y
            canvas.drawLine(lastPos.first,lastPos.second,x,y,midPaint)
            lastPos = currentPos
        }

    }


    fun updateData(trace:Int = Constants.DefaultTraces/2,data:GPRDataMatrix = GPRDataManager.matrixA){
        this.trace = trace
        this.gprData = data
        invalidate()
    }

}
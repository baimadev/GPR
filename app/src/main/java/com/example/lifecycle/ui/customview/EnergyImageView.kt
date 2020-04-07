package com.example.lifecycle.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import com.example.lifecycle.R
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.model.GPRDataMatrix
import com.example.lifecycle.utils.SharedPrefModel
import com.photo.utils.Constants
import kotlin.math.abs


class EnergyImageView (context: Context, attributeSet: AttributeSet): AppCompatImageView(context,attributeSet) {

    val dataInstance = GPRDataManager
    private val textPaint = Paint()
    private val midLineWidth = 8
    var gprData:GPRDataMatrix = GPRDataMatrix.emptyMatrix()
    val midPaint = Paint()
    val linePaint = Paint()
    var defaultX = 0f
    var defaultY = 0f
    var trace = dataInstance.mMidLinePos
    lateinit var lastPos:Pair<Float,Float>

    init {
        gprData.copy(GPRDataManager.matrixT)
        midPaint.color = Color.RED
        midPaint.strokeWidth = 5f
        midPaint.style = Paint.Style.FILL
        linePaint.color = Color.BLACK
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 3f
        linePaint.isAntiAlias = true
        textPaint.textSize = 40f
        textPaint.color = Color.BLACK
        textPaint.style = Paint.Style.FILL
        textPaint.strokeWidth = 10f
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
        defaultX = height/gprData.column.toFloat()
        defaultY = (width-50)/abs(gprData.max - gprData.min)
        lastPos = width/2f to 0f
        canvas!!.drawColor(resources.getColor(R.color.background))
        canvas.drawLine(width/2f,0f,width/2f,height.toFloat(),midPaint)
        if (gprData.matrix.isEmpty()) return
        gprData.matrix[trace].forEachIndexed { index, i ->
            val x = width/2f+ i*defaultY
            val y = (index+1) * defaultX
            val currentPos = x to y
            canvas.drawLine(lastPos.first,lastPos.second,x,y,linePaint)
            lastPos = currentPos
        }
        canvas.drawText("Trace:$trace",20f,height-20f,textPaint)
        canvas.drawText("Distance:${String.format("%.1f",(trace*dataInstance.distanceInterval))}",20f,height-60f,textPaint)

    }

    fun updateData(trace:Int = dataInstance.mMidLinePos,data:GPRDataMatrix? = null){
        this.trace = trace
        data?.let {
            this.gprData.copy(data)
        }
        invalidate()
    }

}
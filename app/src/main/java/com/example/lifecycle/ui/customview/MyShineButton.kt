package com.example.lifecycle.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import com.example.lifecycle.R
import com.sackcentury.shinebuttonlib.ShineButton



class MyShineButton(context: Context, attributeSet: AttributeSet): ShineButton(context,attributeSet) {
    val textSize = 100f

    val paint = Paint()
    init {
        paint.setColor(context.getColor(R.color.white))
        paint.strokeWidth = 10f
        paint.textSize = textSize
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //获取文字所在区域的长方形,textContent是文字内容
        //获取文字所在区域的长方形,textContent是文字内容
        val lRect = Rect()
        val text ="GO!"
        paint.getTextBounds(text, 0,text.length, lRect)
        //用控件所占区域的长方形的宽度，减去文字所在的长方形的跨度，一半的位置就是文字开始的X坐标
        //用控件所占区域的长方形的宽度，减去文字所在的长方形的跨度，一半的位置就是文字开始的X坐标
        val baseLineX = measuredWidth / 2f - lRect.width() / 2f

        val baseline = (measuredHeight - (-paint.fontMetrics.ascent + paint.fontMetrics.descent)) /
                2  - paint.fontMetrics.ascent + 6
        val textWidth = textSize
        val textHeight = textSize
        val x =(width - textWidth)/2f
        val y = (height - textHeight)/2f
        canvas!!.drawText(text,baseLineX,baseline,paint)
    }
}
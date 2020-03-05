package com.example.lifecycle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.media.Image
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.lifecycle.model.Matrix

class GPRImageView(context: Context, attrs: AttributeSet) : View(context,attrs){


    //像素宽度
    var rectWidth = 0f
    //雷达数据 i j
    var data : Matrix = Matrix.emptyMatrix()
    //颜色数组
    var colorArray : Array<Int?> = arrayOfNulls(1000)
    //rgb
    val br = Color.red(resources.getColor(R.color.wh))
    val bg = Color.green(resources.getColor(R.color.wh))
    val bb = Color.blue(resources.getColor(R.color.wh))
    val ba = Color.alpha(resources.getColor(R.color.wh))

    fun setGPRData(gprData:Matrix){
        data = gprData

    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rectWidth = width.toFloat()/1000f

        val paint = Paint()
        paint.color=resources.getColor(R.color.back)

        val paint2= Paint()
        paint2.color = resources.getColor(R.color.wh)

        if(data!=null){
            val rect = RectF(0f,0f,rectWidth,rectWidth)

            for( i in 0 until data.row){  //hang
                val left = i*rectWidth
                val right = left+rectWidth

                val column = data.matrix[i]!!.size
                for( j in 0 until column){  //lie

                    val top = j*rectWidth
                    val bottom = top +rectWidth
                    val q = data.matrix[i]?.get(j)
                    paint.setColor(getColor(q))
                    rect.set(left,top,right,bottom)
                    canvas!!.drawRect(rect,paint)
                }
            }
        }

    }

    fun getColor(data:Float?) : Int{
      //  var color = colorArray[data]
        if(data == null){
            return Color.WHITE
        }else{
            val Q = (15000f - data)/30000f
            val r1 = (Q*br+Q*bg+Q*bb).toInt()
            val g1 = (Q*br+Q*bg+Q*bb).toInt()
            val b1 = (Q*br+Q*bg+Q*bb).toInt()
            val color = Color.argb(ba,r1,g1,b1)
            Log.d("xia","$color")
            return color
        }
    }




}
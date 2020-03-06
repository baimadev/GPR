package com.example.lifecycle

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.lifecycle.model.GPRDataMatrix
import com.ortiz.touchview.TouchImageView


class GPRImageView(context: Context, attrs: AttributeSet) : TouchImageView(context,attrs) {

    //像素宽度
    var rectWidth = 0f
    //雷达数据 i j
    var data : GPRDataMatrix = GPRDataMatrix.emptyMatrix()
    //颜色数组
    var colorArray : Array<Int?> = arrayOfNulls(1000)
    //rgb
    val br = Color.red(resources.getColor(R.color.wh))
    val bg = Color.green(resources.getColor(R.color.wh))
    val bb = Color.blue(resources.getColor(R.color.wh))
    val ba = Color.alpha(resources.getColor(R.color.wh))

    val paint= Paint()

    lateinit var bitmap: Bitmap

    fun setGPRData(gprData:GPRDataMatrix){
        data = gprData

    }

    init {
        paint.color = resources.getColor(R.color.wh)

    }

    @SuppressLint("CheckResult", "DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

         bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val mCanvas = Canvas(bitmap)
        if(data!=null){

                rectWidth = width.toFloat() / data.row
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
                     //   canvas?.drawRect(rect,paint)
                        mCanvas.drawRect(rect,paint)

                    }
                }
            //setImageBitmap(bitmap)
        }

    }

    fun getCurrentBitmap(): Bitmap{
        return bitmap
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
            return color
        }
    }

}
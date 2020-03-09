package com.example.lifecycle.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.example.lifecycle.R
import com.example.lifecycle.model.GPRDataMatrix
import com.ortiz.touchview.TouchImageView
import io.reactivex.Single


class GPRImageView(context: Context, attrs: AttributeSet) : TouchImageView(context,attrs) {

    //像素宽度
    var rectWidth = 0f
    //雷达数据 i j
    var gprData : GPRDataMatrix? = null
    
    //rgb
    var r = Color.red(resources.getColor(R.color.wh))
    var g = Color.green(resources.getColor(R.color.wh))
    var b = Color.blue(resources.getColor(R.color.wh))
    var a = Color.alpha(resources.getColor(R.color.wh))

    val paint= Paint()

    lateinit var bitmap: Bitmap

//    fun setGPRData(gprData:GPRDataMatrix){
//        data = gprData
//    }

    init {
        paint.color = resources.getColor(R.color.wh)
    }

    @SuppressLint("CheckResult", "DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

     fun initImageBitmap(data:GPRDataMatrix) : Single<Bitmap> = Single.fromCallable {
         //setColor(R.color.green)
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val mCanvas = Canvas(bitmap)
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
                    mCanvas.drawRect(rect,paint)
                }
            }
        bitmap
        }

    fun getCurrentBitmap(): Bitmap{
        return bitmap
    }

    fun setColor(colorId :Int){
        r = Color.red(resources.getColor(colorId))
        g = Color.green(resources.getColor(colorId))
        b = Color.blue(resources.getColor(colorId))
        a = Color.alpha(resources.getColor(colorId))
    }

    fun getColor(data:Float?) : Int{
        if(data == null){
            return Color.WHITE
        }else{
            val Q = (15000f - data)/30000f
            val r1 = (Q*r+g+b).toInt()
            val g1 = (r+Q*g+b).toInt()
            val b1 = (r+g+Q*b).toInt()
            val color = Color.argb(a,r1,g1,b1)
            return color
        }
    }

}
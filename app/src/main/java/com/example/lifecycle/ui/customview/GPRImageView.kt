package com.example.lifecycle.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.example.lifecycle.R
import com.example.lifecycle.model.GPRDataMatrix
import com.example.lifecycle.utils.SharedPrefModel
import com.ortiz.touchview.TouchImageView
import io.reactivex.Single
import kotlin.math.abs
import kotlin.math.floor


class GPRImageView(context: Context, attrs: AttributeSet) : TouchImageView(context, attrs) {

    //像素宽高
    var rectWidth = 1f
    var rectHeight = 2f
    //雷达数据 i j
    var gprData: GPRDataMatrix? = null

    //rgb
    var r = Color.red(resources.getColor(R.color.wh))
    var g = Color.green(resources.getColor(R.color.wh))
    var b = Color.blue(resources.getColor(R.color.wh))
    var a = Color.alpha(resources.getColor(R.color.wh))

    val paint = Paint()

    lateinit var bitmap: Bitmap
    lateinit var mCanvas :Canvas

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

    fun initImageBitmap(data: GPRDataMatrix): Single<Bitmap> = Single.fromCallable {
        //setColor(R.color.green)
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        gprData = data
        mCanvas = Canvas(bitmap)
        // rectWidth = 1.5f
        val rect = RectF(0f, 0f, rectWidth, rectHeight)
        for (i in 0 until data.row) {  //hang
            val left = i * rectWidth
            val right = left + rectWidth
            val column = data.matrix[i]!!.size
            for (j in 0 until column) {  //lie
                val top = j * rectHeight
                val bottom = top + rectHeight
                val q = data.matrix[i]?.get(j)
                paint.setColor(getColor(q))
                rect.set(left, top, right, bottom)
                mCanvas.drawRect(rect, paint)
            }
        }
        bitmap
    }

    fun updateData(data: GPRDataMatrix) = Single.fromCallable{
        gprData = data
        drawBitmap()
    }

    fun getCurrentBitmap(): Bitmap {
        return bitmap
    }

    fun getColor(data: Float?): Int {
        return if (data == null) {
            Color.WHITE
        } else {
            val Q = (gprData!!.max - data) / (gprData!!.max - gprData!!.min)
            val r1 = (Q * r + g + b).toInt()
            val g1 = (r + Q * g + b).toInt()
            val b1 = (r + g + Q * b).toInt()
            val color = Color.argb(a, r1, g1, b1)
            color
        }
    }

    private fun getColor2(progress: Int, data: Float?): Int {
        return if (data == null) {
            Color.WHITE
        } else {
            val Q = (gprData!!.max - data) / (gprData!!.max - gprData!!.min)
            val color = HSL_TO_RGB((progress + Q * 90).toInt())
            color
        }
    }

    fun drawBitmap(colorProgress:Int = SharedPrefModel.mHuePos,mode:Int = colorMode1){
        mCanvas = Canvas(bitmap)
        val rect = RectF(0f, 0f, rectWidth, rectHeight)
        for (i in 0 until gprData!!.row) {  //hang
            val left = i * rectWidth
            val right = left + rectWidth
            val column = gprData!!.matrix[i]!!.size
            for (j in 0 until column) {  //lie
                val top = j * rectHeight
                val bottom = top + rectHeight
                val q = gprData!!.matrix[i]?.get(j)
                if(mode == colorMode1){
                    paint.color = getColor(q)
                }else if(mode == colorMode2){
                    paint.color = getColor2(colorProgress, q)
                }
                rect.set(left, top, right, bottom)
                mCanvas.drawRect(rect, paint)
            }
        }
        this.setImageBitmap(bitmap)
    }

    //Convert HSL to RGB
    //H: Hue ( 0 to 360 ) 
    //S: Saturation ( 0 to 1 )
    //L: Ligntness ( 0 to 1 )
    private fun HSL_TO_RGB(H: Int, S: Int = 1, L: Float = 0.9f): Int {
        var C: Float
        if (L <= 0.5) C = 2 * L * S;
        else C = (2 - 2 * L) * S;
        var H2: Float = H / 60f
        var X: Float = C * (1 - abs(H2 % 2 - 1))
        var R: Float
        var G: Float
        var B: Float

        when (H2) {

            in 0..1 -> {
                R = C
                G = X
                B = 0f
            }
            in 1..2 -> {
                R = X
                G = C
                B = 0f
            }
            in 2..3 -> {
                R = 0f
                G = C
                B = X
            }
            in 3..4 -> {
                R = 0f
                G = X
                B = C
            }
            in 4..5 -> {
                R = X
                G = 0f
                B = C
            }
            in 5..6 -> {
                R = C
                G = 0f
                B = X
            }
            else -> {
                R = 0f
                G = 0f
                B = 0f
            }

        }
        var m: Int = (L - 0.5 * C).toInt()
        R += m
        G += m
        B += m
        val r = Math.floor((R * 255).toDouble()).toInt()
        val g = Math.floor((G * 255).toDouble()).toInt()
        val b = Math.floor((B * 255).toDouble()).toInt()
        val color = Color.argb(a, r, g, b)
        return color
    }

    companion object{
        const val colorMode1 = 0xFF1 //黑白
        const val colorMode2 = 0xFF2 //彩色
    }


}
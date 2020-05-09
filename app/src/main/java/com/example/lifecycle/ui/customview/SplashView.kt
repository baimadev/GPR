package com.example.lifecycle.ui.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.annotation.ColorInt
import com.example.lifecycle.R
import kotlin.math.sin
import kotlin.math.sqrt

class SplashView : View {
    private val dp = resources.displayMetrics.density

    private var showPointList = ArrayList<PointF>(5)
    private var controlPointList = ArrayList<PointF>(5)
    private var startPointList = ArrayList<PointF>(5)
    private var endPointList = ArrayList<PointF>(5)
    private var colorList = arrayListOf<Int>(R.color.splash_pink,R.color.splash_red,R.color.splash_green,R.color.splash_orange,R.color.splash_purple)
    private var radiusListDefault = arrayListOf<Float>(20*dp,28*dp,40*dp,60*dp,18*dp)
    private var radiusList = arrayListOf<Float>(0f,0f,0f,0f,0f,0f)
    private val mPaint = Paint()
    private val degreesUnit = 60f
    private val mN = 6
    private val path = Path()

    constructor(ctx: Context) : this(ctx, null)
    constructor(ctx: Context, attributeSet: AttributeSet?) : this(ctx, attributeSet, 0)
    constructor(ctx: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        ctx,
        attributeSet,
        defStyle
    ) {
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.isAntiAlias=true
        initList()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0 until 5){
            drawHexagon(showPointList[i].x,showPointList[i].y,radiusList[i],canvas!!,resources.getColor(colorList[i]))
        }
    }



    fun beginAnim(){
        val valueAnimator = ValueAnimator.ofFloat(0f,1f)
        valueAnimator.duration = 1000L
        valueAnimator.interpolator = AccelerateInterpolator()
        valueAnimator.addUpdateListener {
            val flag = it.animatedValue as Float
            for (i in 0 until 5){
                radiusList[i] = radiusListDefault[i]*flag
                showPointList[i] = secondBessel(flag,startPointList[i],controlPointList[i],endPointList[i])
            }
            postInvalidate()
        }
        valueAnimator.start()
    }

    /**
     * 二阶贝塞尔函数
     */
    private fun secondBessel(t:Float, startPoint: PointF, controlPoint: PointF, endPoint: PointF): PointF {
        val pointF = PointF()
        val tem =1-t
        pointF.x = startPoint.x*tem*tem+2*t*tem*controlPoint.x+t*t*endPoint.x
        pointF.y = startPoint.y*tem*tem+2*t*tem*controlPoint.y+t*t*endPoint.y
        return pointF
    }

    /**
     * 绘制环形六边形
     */
    private fun drawHexagon(
        mCx: Float,
        mCy: Float,
        mR: Float,
        canvas: Canvas,
        @ColorInt color: Int
    ) {

        mPaint.color = color
        val mR1 = mR-3*dp
        val d = (2 * mR * sin(Math.toRadians(degreesUnit / 2.0)))
        val d1 = (2 * mR1 * sin(Math.toRadians(degreesUnit / 2.0)))
        val c = mCy - mR
        val c1 = mCy - mR1
        val y = ((d * d + mCy * mCy - c * c - mR * mR) / (2 * (mCy - c))).toFloat()
        val y1 = ((d1 * d1 + mCy * mCy - c1 * c1 - mR1 * mR1) / (2 * (mCy - c1))).toFloat()
        val x = (mCx + sqrt(-1 * c * c + 2 * c * y + d * d - y * y)).toFloat()
        val x1 = (mCx + sqrt(-1 * c1 * c1 + 2 * c1 * y1 + d1 * d1 - y1 * y1)).toFloat()

        for (i in 0 until mN) {
            canvas.save()
            canvas.rotate(degreesUnit * i, mCx, mCy)
            path.reset()
            path.moveTo(mCx,c1)
            path.lineTo(mCx,c)
            path.lineTo(x,y)
            path.lineTo(x1,y1)
            canvas.drawPath(path,mPaint)
            canvas.restore()
        }

    }

    private fun initList(){
        //pink
        startPointList.add(PointF(400*dp,250*dp))
        controlPointList.add(PointF(50*dp,250*dp))
        endPointList.add(PointF(50*dp,80*dp))
        showPointList.add(PointF(400*dp,250*dp))

        //red
        startPointList.add(PointF(400*dp,70*dp))
        endPointList.add(PointF(100*dp,210*dp))
        controlPointList.add(PointF(400*dp,210*dp))
        showPointList.add(PointF(400*dp,70*dp))

        //green
        startPointList.add(PointF(200*dp,50*dp))
        endPointList.add(PointF(180*dp,300*dp))
        controlPointList.add(PointF(600*dp,200*dp))
        showPointList.add(PointF(200*dp,50*dp))

        //orange
        startPointList.add(PointF(600*dp,10*dp))
        endPointList.add(PointF(300*dp,210*dp))
        controlPointList.add(PointF(800*dp,100*dp))
        showPointList.add(PointF(600*dp,10*dp))

        //purple
        startPointList.add(PointF(50*dp,80*dp))
        endPointList.add(PointF(330*dp,90*dp))
        controlPointList.add(PointF(200*dp,1*dp))
        showPointList.add(PointF(50*dp,80*dp))
    }
}

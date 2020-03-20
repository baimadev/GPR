package com.example.lifecycle.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.example.lifecycle.R
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.model.GPRDataMatrix
import com.example.lifecycle.utils.ColorUtils
import com.example.lifecycle.utils.ImageHelper
import com.example.lifecycle.utils.SharedPrefModel
import com.ortiz.touchview.TouchImageView
import com.photo.utils.Constants
import io.reactivex.Single
import kotlin.math.abs
import kotlin.math.pow


class GPRImageView(context: Context, attrs: AttributeSet) : TouchImageView(context, attrs) {

    //像素宽高
    var rectWidth = 1f
    var rectHeight = 2f
    //雷达数据 i j
    var gprData: GPRDataMatrix = GPRDataMatrix.emptyMatrix()
    // gprBitmap距view四边距离
    val paddingLeft = 80f
    val paddingTop = 120f
    val paddingBottom = 60f
    val paddingRight = 80f
    val timeLinePL = 65f
    val distanceLinePT = 65f
    val lineLength = 10f
    //init flag
    var initFlag = 0
    //中间竖线
    var defaultX = 0f

    val gprPaint = Paint()
    val linePaint = Paint()
    val textPaint = Paint()
    val midPaint = Paint()

    var gprBitmapWidth = 0
    var gprBitmapHeight = 0
    var rawGprBitmap: Bitmap? = null
    var showGprBitmap: Bitmap? = null
    var mainBitmap: Bitmap? = null
    lateinit var gprCanvas :Canvas
    lateinit var mainCanvas :Canvas
    lateinit var onDrawObserver :() -> Unit

    init {
        linePaint.color = Color.BLACK
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 5f
        linePaint.strokeCap = Paint.Cap.SQUARE
        textPaint.textSize = 40f
        textPaint.color = Color.BLACK
        textPaint.style = Paint.Style.FILL
        textPaint.strokeWidth = 10f
        midPaint.color = Color.RED
        midPaint.strokeWidth = 10f
        midPaint.style = Paint.Style.STROKE
        gprBitmapHeight = (rectHeight * SharedPrefModel.samples).toInt()
        gprBitmapWidth = (rectWidth * Constants.DefaultTraces).toInt()
        defaultX = paddingLeft+(gprBitmapWidth/2)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode=MeasureSpec.getMode(widthMeasureSpec)
        val widthSize=MeasureSpec.getSize(widthMeasureSpec)

        val heightMode=MeasureSpec.getMode(heightMeasureSpec)
        val heightSize=MeasureSpec.getSize(heightMeasureSpec)

        var width=(getPaddingLeft()+getPaddingRight()+paddingLeft+paddingRight+gprBitmapWidth).toInt()
        var height=(getPaddingBottom()+getPaddingTop()+paddingBottom+paddingTop+gprBitmapHeight).toInt()

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
    @SuppressLint("CheckResult", "DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        onDrawObserver.invoke()
    }



    fun drawTimeLine(canvas: Canvas) {

        canvas.drawLine(timeLinePL,paddingTop,timeLinePL,paddingTop+gprBitmapHeight,linePaint)
        textPaint.textAlign = Paint.Align.RIGHT
        var horizontalLineNumber = SharedPrefModel.timeWindow.toInt()/10
        val horizontalLineSpace =  SharedPrefModel.samples/SharedPrefModel.timeWindow*10*rectHeight
        var horizontalLineStartY = paddingTop
        for(i in 0 .. horizontalLineNumber){
            val startY = horizontalLineStartY+(i*horizontalLineSpace)
            canvas.drawLine(timeLinePL-lineLength,startY,timeLinePL,startY,linePaint)
            canvas.drawText((i*10).toString(),timeLinePL-20,startY+15,textPaint)
        }
        canvas.drawLine(timeLinePL-lineLength,paddingTop+gprBitmapHeight,timeLinePL,paddingTop+gprBitmapHeight,linePaint)
        canvas.drawText((String.format("%.2f",SharedPrefModel.timeWindow))+"ns",timeLinePL+80,paddingTop+gprBitmapHeight+50,textPaint)

    }

    fun drawDistanceLine(canvas: Canvas) {
        canvas.drawLine(paddingLeft,distanceLinePT,paddingLeft+gprBitmapWidth,distanceLinePT,linePaint)
        val distanceNumber = (SharedPrefModel.distanceInterval * Constants.DefaultTraces).toInt()/5
        val verticalLineSpace = 5/SharedPrefModel.distanceInterval*rectWidth
        var startX = paddingLeft
        textPaint.textAlign = Paint.Align.CENTER
        for(i in 0..distanceNumber){
            val posX = startX + i*verticalLineSpace
            canvas.drawLine(posX,distanceLinePT-lineLength,posX,distanceLinePT,linePaint)
            canvas.drawText(String.format("%.1f",i*5.toFloat()),posX-10,distanceLinePT-20,textPaint)
        }
        canvas.drawLine(paddingLeft+gprBitmapWidth,distanceLinePT-lineLength,paddingLeft+gprBitmapWidth,distanceLinePT,linePaint)
        canvas.drawText(String.format("%.1f",SharedPrefModel.distanceInterval*Constants.DefaultTraces)+"m",paddingLeft+gprBitmapWidth,distanceLinePT-20,textPaint)

        val traceSpace = rectWidth*100
        val traceNumber = Constants.DefaultTraces /100
        for(i in 0..traceNumber){
            val posX = startX + i*traceSpace
            canvas.drawLine(posX,distanceLinePT+lineLength,posX,distanceLinePT,linePaint)
            if(i == traceNumber){
                canvas.drawText((i*100).toString()+"道",posX+10,distanceLinePT+45,textPaint)
            }else{
                canvas.drawText((i*100).toString(),posX-10,distanceLinePT+45,textPaint)
            }
        }
    }

    fun drawDepthLine(canvas: Canvas){
        val depth = SharedPrefModel.timeWindow * (2.99792458E8 / Math.sqrt(6.0)) / 2.0E9
        val startX = paddingLeft+gprBitmapWidth+20
        canvas.drawLine(startX,paddingTop,startX,paddingTop+gprBitmapHeight,linePaint)
        textPaint.textAlign = Paint.Align.LEFT
        var horizontalLineNumber = (depth/0.5).toInt()
        val horizontalLineSpace =  gprBitmapHeight/depth*0.5
        var horizontalLineStartY = paddingTop
        for(i in 0 .. horizontalLineNumber){
            val startY = (horizontalLineStartY+(i*horizontalLineSpace)).toFloat()
            canvas.drawLine(startX,startY,startX+lineLength,startY,linePaint)
            canvas.drawText((i*0.5).toString(),startX+20,startY+15,textPaint)
        }
        canvas.drawLine(startX,paddingTop+gprBitmapHeight,startX+lineLength,paddingTop+gprBitmapHeight,linePaint)
        canvas.drawText((String.format("%.2f",depth))+"m",startX-50,paddingTop+gprBitmapHeight+50,textPaint)

    }

    fun initImageBitmap(data: GPRDataMatrix): Single<Bitmap> = Single.fromCallable {
        rawGprBitmap = Bitmap.createBitmap(gprBitmapWidth, gprBitmapHeight, Bitmap.Config.ARGB_8888)
        mainBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        showGprBitmap = rawGprBitmap
        gprData.copy(data)
        gprCanvas = Canvas(rawGprBitmap!!)
        mainCanvas = Canvas(mainBitmap!!)
        //先画线和背景 画在了maiBitmap上
        drawTimeLine(mainCanvas)
        drawDistanceLine(mainCanvas)
        drawDepthLine(mainCanvas)
        //绘制gpr图像 在showBitmap上
        drawBitmap()
        //绘制gpr在mainBitmap上
        mainCanvas.drawBitmap(showGprBitmap!!,paddingLeft,paddingTop,gprPaint)
        mainBitmap
    }

    //滤波数据更新
    fun updateData(data: GPRDataMatrix) = Single.fromCallable{
        gprData.copy(data)
        drawBitmap()
        drawGprBitmapOnMain()
    }

    fun drawGprBitmapOnMain(){
        mainCanvas.drawBitmap(showGprBitmap!!,paddingLeft,paddingTop,gprPaint)
        this.setImageBitmap(mainBitmap)
    }

    fun drawBitmap(colorProgress:Int = SharedPrefModel.mHuePos){
        gprCanvas = Canvas(rawGprBitmap!!)
        val rect = RectF(0f, 0f, rectWidth, rectHeight)
        for (i in 0 until gprData.row) {  //hang
            val left = i * rectWidth
            val right = left + rectWidth
            val column = gprData.matrix[i].size
            for (j in 0 until column) {  //lie
                val top = j * rectHeight
                val bottom = top + rectHeight
                val q = gprData.matrix[i].get(j)
                gprPaint.color = ColorUtils.coloj(q,gprData,colorProgress)
                rect.set(left, top, right, bottom)
                gprCanvas.drawRect(rect, gprPaint)
            }
        }

        val bitmap = ImageHelper.handleImageEffec(rawGprBitmap!!, SharedPrefModel.mSaturation, SharedPrefModel.mLum)
        showGprBitmap = bitmap

    }


}

fun dataToFactor(data:Float,matrix:GPRDataMatrix,k:Float = 0.8f):Float{
    val max = colorFunction(matrix.max,k)
    val min = colorFunction(matrix.min,k)
    val num = colorFunction(data,k)
    if((max - num)/(max - min)<0 || (max - num)/(max - min)>1){
        Log.d("xia","${(max - num)/(max - min)} $data $max $min ${matrix.max} ${matrix.min}")
    }
    return (max - num)/(max - min)
}

fun colorFunction(num:Float,k: Float): Float {
    return if(num < 0) {
        -1* abs(num).toDouble().pow(k.toDouble()).toFloat()
    }else if(num >0){
        abs(num).toDouble().pow(k.toDouble()).toFloat()
    }else{
        0f
    }
}
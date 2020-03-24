package com.example.lifecycle.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Matrix.MSCALE_X
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import com.example.lifecycle.R
import com.example.lifecycle.utils.SharedPrefModel
import com.photo.utils.Constants


class SqureImagView (context: Context, attributeSet: AttributeSet): AppCompatImageView(context,attributeSet),ScaleGestureDetector.OnScaleGestureListener {

    private val MAX_SCALE_TIME = 4f
    private val mBaseScale = 1.2f
    private var downX  = 0
    private var gprPaddingTop = 120f
    private var squareWidth = 300f
    private var squareHeight = 300f
    private var squarePaddingTop = gprPaddingTop + 30
    private val mMatrix = Matrix()
    private var lastX  = 0
    private var lastY  = 0
    private var pointer  = false
    val paddingLeft = 80f
    val midPaint = Paint()
    val bitmap =Bitmap.createBitmap(500,500,Bitmap.Config.ARGB_8888)
    var scaleTemp = 1f
    var isScale = false
    private var scaleDetector : ScaleGestureDetector? = null
    //像素宽高
    var rectWidth = 1f
    var rectHeight = 2f
    val gprImageViewWidth = rectWidth*Constants.DefaultTraces
    val gprImageViewHeight = rectHeight*SharedPrefModel.samples
    init {
        val canvas = Canvas(bitmap)
        canvas.drawColor(context.getColor(R.color.lineColor))
        midPaint.color = Color.RED
        midPaint.strokeWidth = 10f
        midPaint.style = Paint.Style.STROKE
        scaleDetector = ScaleGestureDetector(context,this)
        scaleType = ScaleType.MATRIX
        this.setImageBitmap(bitmap)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(squareWidth.toInt(),squareHeight.toInt())
    }
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(true, 0, 0, width, height)
    }

    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.rawX.toInt()
        val y = event!!.rawY.toInt()
        scaleDetector?.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录触摸点坐标
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                // 计算偏移量
                var offsetX: Int = x - lastX
                var offsetY: Int = y - lastY

                if((left+offsetX)<(30+paddingLeft)){
                    offsetX = 0
                }
                if((right+offsetX)>(30+paddingLeft+gprImageViewWidth)){
                    offsetX = 0
                }

                if((top+offsetY)<(squarePaddingTop)){
                    offsetY = 0
                }
                if((bottom+offsetY)>(squarePaddingTop+gprImageViewHeight)){
                    offsetY = 0
                }
                layout(left + offsetX, top+offsetY , right + offsetX, bottom+offsetY)
                invalidate()
                //重新设置初始化坐标
                lastX = x
                lastY = y
            }
        }

        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        lastSpanX = detector!!.currentSpanX
        lastSpanY = detector.currentSpanY
        return true;
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
    }

    var lastSpanX = 0f
    var lastSpanY = 0f
    override fun onScale(detector: ScaleGestureDetector?): Boolean {

        // 获取缩放因子
        // 获取缩放因子
        var scaleFactor = detector!!.scaleFactor
        val scale: Float = getScale()
        val currentSpanX = detector.currentSpanX
        val currentSpanY = detector.currentSpanY
        val newWidth = currentSpanX /lastSpanX
        val newHeight =  currentSpanY/lastSpanY

//            mMatrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
//            borderAndCenterCheck()
//            imageMatrix = mMatrix

        Log.d("xia"," ps:${detector.previousSpan}  cs${detector.currentSpan} scale  $scale")
        val scaleN =  scaleFactor /scale
        mMatrix.postScale(newWidth, newHeight, detector.focusX, detector.focusY)
        imageMatrix = mMatrix
        return true
    }

    private fun getScale(): Float {
        val values = FloatArray(9)
        mMatrix.getValues(values)
        return values[MSCALE_X]
    }

    /**
     * 获得图片放大缩小以后的宽和高
     */
    private fun getMatrixRectF(): RectF {
        val rectF = RectF()
        val drawable = drawable
        if (drawable != null) {
            rectF[0f, 0f, drawable.intrinsicWidth.toFloat()] = drawable.intrinsicHeight.toFloat()
            mMatrix.mapRect(rectF)
        }
        return rectF
    }

    private fun borderAndCenterCheck() {
        val rect: RectF = getMatrixRectF()
        var deltaX = 0f
        var deltaY = 0f
        val viewWidth = width
        val viewHeight = height
        // 缩放时进行边界检测，防止出现白边
        if (rect.width() >= viewWidth) {
            if (rect.left > 0) {
                deltaX = -rect.left
            }
            if (rect.right < viewWidth) {
                deltaX = viewWidth - rect.right
            }
        }
        if (rect.height() >= viewHeight) {
            if (rect.top > 0) {
                deltaY = -rect.top
            }
            if (rect.bottom < viewHeight) {
                deltaY = viewHeight - rect.bottom
            }
        }
        // 如果宽度或者高度小于控件的宽或者高；则让其居中
        if (rect.width() < viewWidth) {
            deltaX = viewWidth / 2f - rect.right + rect.width() / 2f
        }
        if (rect.height() < viewHeight) {
            deltaY = viewHeight / 2f - rect.bottom + rect.height() / 2f
        }
        mMatrix.postTranslate(deltaX, deltaY)
    }

}




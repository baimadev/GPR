package com.example.lifecycle.utils

import android.graphics.*

object ImageHelper {
    fun  handleImageEffec(bm :Bitmap, hue: Float, saturation:Float,lum:Float):Bitmap {
        //设置颜色的色调
        val hueMatrix =  ColorMatrix()
        //第一个参数，系统分别使用0、1、2来代表Red、Green、Blue三种颜色的处理；而第二个参数，就是需要处理的值
        hueMatrix.setRotate(0, hue)
        hueMatrix.setRotate(1, hue)
        hueMatrix.setRotate(2, hue)

        //设置颜色的饱和度
        val saturationMatrix = ColorMatrix()
        //saturation参数即代表设置颜色的饱和度的值，当饱和度为0时，图像就变成灰度图像了
        saturationMatrix.setSaturation(saturation);

        //设置颜色的亮度
        val lumMatrix =  ColorMatrix()
        lumMatrix.setScale(lum, lum, lum, 1f)

        //将矩阵的作用效果混合，从而叠加处理效果
        val imageMatrix = ColorMatrix()
        imageMatrix.postConcat(hueMatrix)
        imageMatrix.postConcat(saturationMatrix)
        imageMatrix.postConcat(lumMatrix)

        /**
         * 设置好处理的颜色矩阵后，通过使用Paint类的setColorFilter()方法，将通过imageMatrix构造的
         * ColorMatrixColorFilter对象传递进去，并使用这个画笔来绘制原来的图像，从而将颜色矩阵作用到原图中
         */
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(imageMatrix);
        /**
         * Android系统也不允许直接修改原图，类似Photoshop中的锁定，必须通过原图创建一个同样大小的Bitmap，并将
         * 原图绘制到该Bitmap中，以一个副本的形式来修改图像。
         */
        val bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bm, 0f, 0f ,paint)
        return bitmap
    }

}
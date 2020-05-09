package com.example.lifecycle.utils

import java.nio.file.Files.size
import kotlin.math.cos
import kotlin.math.sin

const val pi = 3.1415926

fun fft(raw:FloatArray): Pair<Array<Float>, Array<Float>> {
    val size = raw.size
    val reList  = Array<Float>(size){0f}
    val imList  = Array<Float>(size){0f}
    for(i in 0 until  size){
        var re=0.0
        var im=0.0
        for(j in 0 until size){
            re+= raw[j]*cos(2*pi*i*j/size.toFloat())
            im+= raw[j]*sin(2*pi*i*j/size.toFloat())
        }
        reList[i] = re.toFloat()
        imList[i] = im.toFloat()
    }
    return reList to imList
}

fun iFFT(reList:FloatArray,imList:FloatArray): Pair<Array<Float>, Array<Float>> {
    val size = reList.size
    val realList  = Array<Float>(size){0f}
    val imagList  = Array<Float>(size){0f}
    for(i in 0 until  size){
        var re=0.0
        var im=0.0
        for(j in 0 until size){
            re+= reList[j]*cos(2*pi*i*j/size.toFloat())-imList[j]* sin(i*j*2*pi/size.toFloat())
            im+= reList[j]*sin(2*pi*i*j/size.toFloat())+imList[j]*cos(2*pi*i*j/size.toFloat())
        }
        realList[i] = re.toFloat()/size.toFloat()
        imagList[i] = im.toFloat()/size.toFloat()
    }
    return realList to imagList
}
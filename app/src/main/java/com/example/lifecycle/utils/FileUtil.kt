package com.example.lifecycle.utils

import android.os.Build
import android.system.Os.accept
import android.util.Log
import com.example.lifecycle.model.Matrix
import io.reactivex.Flowable
import io.reactivex.Single
import java.io.*
import java.lang.Exception
import java.nio.charset.Charset

object FileUtil {


    /**
     * 读取了前1000个道
     */
    fun readFileToMatrix2(file: File): Matrix {

        val bufferReader = file.bufferedReader(Charset.forName("ASCII"))

        val array = arrayOfNulls<FloatArray>(1000)

        for (i in 0 until 1000) {
            val str = bufferReader.readLine()
            if (str.isNotEmpty() && str.isNotBlank()) {
                val spString = str.split("\\s+".toRegex())
                    .map {
                        var a = 0f
                        if (it.isNotEmpty() && it.isNotBlank()) { a = it.toFloat() }
                        a
                    }.toFloatArray()
                array[i] = spString
            }

        }
        return Matrix(1000, 1500, array)
    }


}
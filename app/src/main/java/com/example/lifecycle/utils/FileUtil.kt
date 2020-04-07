package com.example.lifecycle.utils

import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.model.GPRDataMatrix
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream


object FileUtil {

    private const val limitedivi = 10000.0
    var divi = 1.0f
    val instance = GPRDataManager

    /**
     * 读取rad 文件 预加载数据
     */

    fun readRad(file: File) {
        val stringBuilder = StringBuilder()
        val bufferedReader = file.bufferedReader()
        while (true) {
            val line = bufferedReader.readLine() ?: break
            stringBuilder.append("\n" + line)
        }
        val indexOf: Int = stringBuilder.indexOf("SAMPLES")

        instance.samples =
            cuantoes(
                stringBuilder.substring(
                    indexOf + 8,
                    stringBuilder.indexOf("\n", indexOf)
                )
            ).toInt()
        val indexOf2: Int = stringBuilder.indexOf("LAST TRACE")
        instance.lastTrace =
            cuantoes(
                stringBuilder.substring(
                    indexOf2 + 11,
                    stringBuilder.indexOf("\n", indexOf2)
                )
            ).toInt()
       instance.col = instance.lastTrace
        if (instance.col.toDouble() > this.limitedivi) {
            val d2 = instance.col.toFloat()
            d2.isNaN()
            divi = (limitedivi / d2).toFloat()
        } else {
            divi = 1.0f
        }
        val indexOf3: Int = stringBuilder.indexOf("TIMEWINDOW")
        instance.timeWindow =
            cuantoes(stringBuilder.substring(indexOf3 + 11, stringBuilder.indexOf("\n", indexOf3)))
        val indexOf4: Int = stringBuilder.indexOf("DISTANCE INTERVAL")
        instance.distanceInterval =
            cuantoes(stringBuilder.substring(indexOf4 + 18, stringBuilder.indexOf("\n", indexOf4)))
    }

    /**
     * 读取rd3 文件
     */
    fun readRd3(file: File, part: Int): GPRDataMatrix {

        var max = Float.MIN_VALUE
        var min = Float.MAX_VALUE
        val col = instance.samples
        val row = instance.defaultTraces
        val array = Array(row) { FloatArray(col) }
        val dataInputStream = DataInputStream(FileInputStream(file))

        //先读取part*row行
        val partRow = if (part == 0) {
            0
        } else {
            (part - 1) * row
        }

        dataInputStream.skip(partRow * col.toLong() * 2)

        for (i in 0 until row) {
            for (j in 0 until col) {
                val byte1 = dataInputStream.readByte().toFloat()
                val byte2 = dataInputStream.readByte().toFloat()
                if (!byte1.isNaN() && !byte2.isNaN()) {
                    val d = (byte2 * 256f) + (byte1 * 1f)
                    array[i][j] = d
                    val drr = array[i]
                    drr[j] = drr[j] + d

                }
            }
            //存储最大最小值
            val cMax = array[i].max()!!
            val cMin = array[i].min()!!
            if (max < cMax) {
                max = cMax
            }
            if (min > cMin) {
                min = cMin
            }
        }


        dataInputStream.close()

        return GPRDataMatrix(row, array[0].size, array, max, min)
    }


    private fun cuantoes(str: String): Float {
        return try {
            val parseDouble = str.replace(",".toRegex(), ".").toFloat()
            if (parseDouble.isNaN()) {
                0f
            } else parseDouble
        } catch (unused: NumberFormatException) {
            0f
        }
    }


}
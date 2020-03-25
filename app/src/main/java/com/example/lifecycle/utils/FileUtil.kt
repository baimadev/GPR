package com.example.lifecycle.utils

import android.util.Log
import com.example.lifecycle.model.GPRDataMatrix
import com.photo.utils.Constants
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

object FileUtil {

    private const val limitedivi = 10000.0
    var divi = 1.0f
    /**
     * 读取rad 文件 预加载数据
     */

    fun readRad(file: File){
     
        val stringBuilder = StringBuilder()
        val bufferedReader = file.bufferedReader()
        while (true){
            val line = bufferedReader.readLine() ?: break
            stringBuilder.append("\n"+line)
        }
        val indexOf: Int = stringBuilder.indexOf("SAMPLES")

        SharedPrefModel.samples =
            cuantoes(stringBuilder.substring(indexOf + 8, stringBuilder.indexOf("\n", indexOf))).toInt()
        val indexOf2: Int = stringBuilder.indexOf("LAST TRACE")
        SharedPrefModel.lastTrace =
            cuantoes(stringBuilder.substring(indexOf2 + 11, stringBuilder.indexOf("\n", indexOf2))).toInt()
        SharedPrefModel.col = SharedPrefModel.lastTrace
        if (SharedPrefModel.col.toDouble() > this.limitedivi) {
            val d2 = SharedPrefModel.col.toFloat()
            d2.isNaN()
            divi = (limitedivi / d2).toFloat()
        } else {
            divi = 1.0f
        }
        SharedPrefModel.col2 = (SharedPrefModel.col.toFloat() * divi).toInt()
        SharedPrefModel.samples2 = (SharedPrefModel.samples.toFloat() * divi).toInt()
        val indexOf3: Int = stringBuilder.indexOf("TIMEWINDOW")
        SharedPrefModel.timeWindow = cuantoes(stringBuilder.substring(indexOf3 + 11, stringBuilder.indexOf("\n", indexOf3)))
        val indexOf4: Int = stringBuilder.indexOf("DISTANCE INTERVAL")
        SharedPrefModel.distanceInterval = cuantoes(stringBuilder.substring(indexOf4 + 18, stringBuilder.indexOf("\n", indexOf4)))
    }

    /**
     * 读取rd3 文件
     */
    fun readRd3(file: File){

        val array = DoubleArray(SharedPrefModel.samples)
        val dataInputStream = DataInputStream(FileInputStream(file))

        for(i in 0 until SharedPrefModel.samples ){
            val byte1 = dataInputStream.readByte().toDouble()
            val byte2 = dataInputStream.readByte().toDouble()
            if(!byte1.isNaN() && !byte2.isNaN()){
                val d = (byte2 * 256.0) + (byte1 * 1.0)
                array[i] = (byte2 * 256.0) + (byte1 * 1.0)
            }
        }
    }

    /**
     * 读取了前row个道
     */
    fun readFileToMatrix2(file: File,row: Int): GPRDataMatrix {

        val bufferReader = file.bufferedReader(Charset.forName("ASCII"))
        var max = Float.MIN_VALUE
        var min = Float.MAX_VALUE
        val col = SharedPrefModel.samples
        val array = Array(row){FloatArray(col)}
        val length = file.length()

        for (i in 0 until row) {
            val str = bufferReader.readLine()
            if (str.isNotEmpty() && str.isNotBlank()) {
                val spString = str.split("\\s+".toRegex())
                    .map {
                        var a = 0f
                        if (it.isNotEmpty() && it.isNotBlank()) { a = it.toFloat() }
                        a
                    }.toFloatArray()
                //存储最大最小值
                val cMax = spString.max()!!
                val cMin = spString.min()!!
                if(max < cMax){
                    max = cMax
                }
                if(min > cMin){
                    min = cMin
                }
                array[i] = spString
            }

        }
        return GPRDataMatrix(row, array[0]!!.size, array,max,min)
    }


    fun readFileToMatrix(file: File,part: Int): GPRDataMatrix {

        val bufferReader = file.bufferedReader(Charset.forName("ASCII"))
        var max = Float.MIN_VALUE
        var min = Float.MAX_VALUE
        val col = SharedPrefModel.samples
        val row = SharedPrefModel.defaultTraces
        val array = Array(row){ FloatArray(col)}

        val partRow = (part-1)*row
        for( j in 0 until partRow){
            bufferReader.readLine()
        }

        for (i in 0 until row) {
            val str = bufferReader.readLine()
            if (str.isNotEmpty() && str.isNotBlank()) {
                val spString = str.split("\\s+".toRegex())
                    .map {
                        var a = 0f
                        if (it.isNotEmpty() && it.isNotBlank()) { a = it.toFloat() }
                        a
                    }.toFloatArray()
                //存储最大最小值
                val cMax = spString.max()!!
                val cMin = spString.min()!!
                if(max < cMax){
                    max = cMax
                }
                if(min > cMin){
                    min = cMin
                }
                array[i] = spString
            }
        }
        return GPRDataMatrix(row, array[0].size, array,max,min)
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
package com.example.lifecycle.utils

import com.example.lifecycle.model.GPRDataMatrix
import java.io.*
import java.nio.charset.Charset

object FileUtil {


    /**
     * 读取了前row个道
     */
    fun readFileToMatrix2(file: File,row: Int): GPRDataMatrix {

        val bufferReader = file.bufferedReader(Charset.forName("ASCII"))

        val array = arrayOfNulls<FloatArray>(row)

        for (i in 0 until row) {
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
        return GPRDataMatrix(row, array[0]!!.size, array)
    }


}
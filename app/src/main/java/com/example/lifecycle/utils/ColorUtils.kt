package com.example.lifecycle.utils

import android.graphics.Color
import android.graphics.Matrix
import com.example.lifecycle.model.GPRDataMatrix
import com.example.lifecycle.ui.customview.dataToFactor

object ColorUtils {

    var colores = IntArray(2000)
    var chivocolor = 1.0 //颜色对比度（0..1）
    var colorines = Array(12){IntArray(2000)}
    
    /**
     * colorj
     * @param d dataValue
     * @param mode 色相选择（0 .. 12）
     * @return
     */
    fun coloj(d: Float, matrix:GPRDataMatrix,mode:Int = 0): Int {
        val pos = dataToFactor(d,matrix,0.8f)*1536  //0..1536
        return colorines[mode][pos.toInt()]
    }

    
    /**
     * 初始化色域
     */
    //色域   colores[2000]
    fun initColoracion() {
        var i = 0
        for (i2 in 255 downTo 0) {
            colores[i] = Color.rgb(255, 0, i2)
            i++
        }
        for (i3 in 0..255) {
            colores[i] = Color.rgb(255, i3, 0)
            i++
        }
        for (i4 in 255 downTo 0) {
            colores[i] = Color.rgb(i4, 255, 0)
            i++
        }
        for (i5 in 0..255) {
            colores[i] = Color.rgb(0, 255, i5)
            i++
        }
        for (i6 in 255 downTo 0) {
            colores[i] = Color.rgb(0, i6, 255)
            i++
        }
        for (i7 in 0..255) {
            colores[i] = Color.rgb(i7, 0, 255)
            i++
        }
        coloracion2()
    }

    /**
     * 初始化色域
     */
    fun coloracion2() {
        var d = 0.0
        while (d < 1000.0) {
            val pow =
                Math.pow(Math.sin(0.0015707963267949 * d), chivocolor)
            val i = (128.0 * pow).toInt()
            val i2 = d.toInt()
            val i3 = i2 + 1000
            val i4 = i + 128
            colorines[0][i3] = Color.rgb(i4, i4, i4)
            val i5 = 999 - i2
            val i6 = 128 - i
            colorines[0][i5] = Color.rgb(i6, i6, i6)
            val i7 = (256.0 * pow).toInt()
            colorines[6][i3] = Color.rgb(i7, i7, i7)
            colorines[6][i5] = Color.rgb(i7, i7, i7)
            val i8 = (512.0 * pow).toInt()
            val i9 = i8 + 768
            colorines[1][i3] = colores[i9]
            val i10 = 768 - i8
            colorines[1][i5] = colores[i10]
            colorines[2][i3] = colores[(i9 + 256) % 1536]
            colorines[2][i5] = colores[(i10 + 256) % 1536]
            colorines[3][i3] = colores[(i9 + 512) % 1536]
            colorines[3][i5] = colores[(i10 + 512) % 1536]
            colorines[4][i3] = colores[(i9 + 768) % 1536]
            colorines[4][i5] = colores[(i10 + 768) % 1536]
            colorines[5][i3] = colores[(i9 + 1024) % 1536]
            colorines[5][i5] = colores[(i10 + 1024) % 1536]
            var i11 = (pow * 1279.0).toInt() + 256
            if (i11 >= 1536) {
                i11 -= 1536
            }
            colorines[7][i3] = colores[i11]
            colorines[7][i5] = colores[i11]
            var i12 = i11 + 256
            if (i12 >= 1536) {
                i12 -= 1536
            }
            colorines[8][i3] = colores[i12]
            colorines[8][i5] = colores[i12]
            var i13 = i12 + 256
            if (i13 >= 1536) {
                i13 -= 1536
            }
            colorines[9][i3] = colores[i13]
            colorines[9][i5] = colores[i13]
            var i14 = i13 + 256
            if (i14 >= 1536) {
                i14 -= 1536
            }
            colorines[10][i3] = colores[i14]
            colorines[10][i5] = colores[i14]
            var i15 = i14 + 256
            if (i15 >= 1536) {
                i15 -= 1536
            }
            colorines[11][i3] = colores[i15]
            colorines[11][i5] = colores[i15]
            d += 1.0
        }
    }
}


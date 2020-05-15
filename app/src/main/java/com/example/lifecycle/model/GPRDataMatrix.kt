package com.example.lifecycle.model

import android.util.Log
import com.example.lifecycle.utils.*
import io.reactivex.Single
import timber.log.Timber
import kotlin.math.*

/**
 * 按行存储
 */
data class GPRDataMatrix(
    var row: Int,
    var column: Int,
    var matrix: Array<FloatArray>,
    var max: Float,
    var min: Float
) {

    val dataInstance = GPRDataManager
    var imageList: Array<FloatArray?>? = null
    fun clone(): GPRDataMatrix {
        val newMatrix = matrix.clone()
        return GPRDataMatrix(row, column, newMatrix, max, min)
    }

    fun copy(gprDataMatrix: GPRDataMatrix) {
        row = gprDataMatrix.row
        column = gprDataMatrix.column
        matrix = Array(row) { it -> gprDataMatrix.matrix[it].copyOf() }
        max = gprDataMatrix.max
        min = gprDataMatrix.min
    }


    //时间零点计算 samples每一道的数据个数
    fun timeZero(): Float {
        var i = 1
        while (true) {
            if (i >= dataInstance.samples) {
                i = 0
                break
            } else if (matrix[0][i] < matrix[0][i - 1] - 400.0) {
                break
            } else {
                i++
            }
        }
        var i2 = i + 1
        while (true) {
            if (i2 >= dataInstance.samples) {
                break
            } else if (matrix[0][i2] > matrix[0][i2 - 1]) {
                i = i2
                break
            } else {
                i2++
            }
        }
        if (i > 2) {
            i -= 2
        }
        return i.toFloat()
    }

    /**
     * 时间零点校正
     */
    fun timeZeroCorrect(offset: Int) {
        val newMatrix = Array(row) { FloatArray(column - offset) }
        for (i in 0 until row) {
            for (j in offset until column) {
                newMatrix[i][j - offset] = matrix[i][j]
            }
        }
        matrix = newMatrix
        column -= offset
        updateMM()
    }

    //DC直流分量校正 垂直滤波
    fun DCFiliter() = Single.fromCallable {
        var num = 0
        var average = 0f
        val startJ = column * 0.5.toInt()
        for (i in 0 until row) {  //hang
            for (j in startJ until column) {  //lie
                average += matrix[i][j]
                num++
            }
        }
        average /= num

        for (i in 0 until row) {  //hang
            for (j in 0 until column) {  //lie
                matrix[i][j] -= average
            }
        }
        updateMM()
    }


    //反正切过滤器
    fun AtanFiliter(truncation: Float) = Single.fromCallable {
        for (i in 0 until row) {  //hang
            for (j in 0 until column) {  //lie
                matrix[i][j] = atan(matrix[i][j] * 0.001f * truncation)
            }
        }
        updateMM()
    }

    //道内均衡
    fun tfFiliter(truncation: Float) = Single.fromCallable {
        val M = truncation.toInt()
        for (i in 0 until row) {  //hang
            var average = 0f
            for (j in 0 until column) {  //lie
                if(i==5){Log.e("xia","before matrix ${matrix[i][j]}")}
                when {
                    j - M < 0 -> {
                        for (m in 0..j + M) {
                            average += abs(matrix[i][m])
                        }
                        val number = j + M + 1
                        matrix[i][j] = matrix[i][j] / (average / number)
                        average = 0f
                    }
                    j + M >= column -> {
                        for (m in j - M until column) {
                            average +=abs(matrix[i][m])
                        }
                        val number = column - (j - M)
                        matrix[i][j] = matrix[i][j] / (average / number)
                        average = 0f
                    }
                    else -> {
                        for (m in j - M..j + M) {
                            average += abs(matrix[i][m])
                        }
                        matrix[i][j] = matrix[i][j] / (average / (2 * M + 1))
                        average = 0f
                    }
                }
                if(i==5){Log.e("xia","after matrix ${matrix[i][j]}")}
            }
        }
        updateMM()
    }


    //电位增益滤波器
    fun PotentialGainFilter(truncation: Float) = Single.fromCallable {

        for (i in 0 until row) {  //hang
            for (j in 0 until column) {  //lie
                val number = matrix[i][j]
                matrix[i][j] =
                    (sgn(number) * (abs(number).toDouble().pow(truncation.toDouble()))).toFloat()
            }

        }
        updateMM()
    }

    //垂直推导滤波
    fun VerticalDerivationFilter(k: Int) = Single.fromCallable {

        for (i in 0 until row) {  //hang
            for (j in 1 until column) {  //lie
                if (k != 0) {
                    matrix[i][j] = matrix[i][j] - matrix[i][j - 1]
                }
                val d3 = matrix[i][j] / max
                matrix[i][j] = atan(d3 * k)

            }
            matrix[i][0] = 0f
        }
        updateMM()
    }

    //平滑  计算垂直分量 移动平均值
    fun smoothLine(suavitoh: Float) = Single.fromCallable {
        var i: Int
        val i2: Int = row
        val i3: Int = column
        val f: Float = 50.0f
        val dArr = FloatArray(i2)
        val dArr2 = FloatArray(i2)
        var i4 = 0
        while (i4 < i2) {
            for (i5 in 0 until i3) {
                dArr[i5] = matrix[i4][i5]
            }
            var i6 = 0
            while (true) {
                i = i4
                if (i6.toFloat() >= suavitoh) {
                    break
                }
                dArr2[0] = dArr[0]
                val i7 = i2 - 1
                dArr2[i7] = dArr[i7]
                var i8 = 1
                while (i8 < i7) {
                    val i9 = i8 + 1
                    dArr2[i8] = (dArr[i9] + dArr[i8 - 1] + dArr[i8] * 2f) / 4f
                    i8 = i9
                }
                for (i10 in 0 until i3) {
                    dArr[i10] = dArr2[i10]
                }
                i6++
                i4 = i
            }
            for (i11 in 0 until i3) {
                matrix[i][i11] = dArr[i11]
            }
            i4 = i + 1
        }
        updateMM()
    }

    //傅里叶
    fun frequency(fouri: Int = 10) = Single.fromCallable {
        val imagList = arrayOfNulls<FloatArray>(row)
        for (i in 0 until row) {  //hang
            val pair = fft(matrix[i])
            imagList[i] = pair.second.toFloatArray()
            matrix[i] = pair.first.toFloatArray()
        }
        this.imageList = imagList
        updateMM()
    }

    //反傅里叶
    fun iFrequency() {
        if (imageList == null) return
        for (i in 0 until row) {
            matrix[i] = iFFT(matrix[i], imageList!![i]!!).first.toFloatArray()
        }
        updateMM()
    }

    //FIR
    fun firFiliter(wn: WindowType, band: Int, fln: Float, fhn: Float) = Single.fromCallable {
        val h = getFirFilterFactor(wn, band, fln, fhn)

        for (i in 0 until row) {  //hang
            if (i == 50) {
                Log.e("xia", "before matrix ${matrix[i][50]}  h ${h[i]}")
            }
            matrix[i] = juanji(h, matrix[i])
            if (i == 50) {
                Log.e("xia", "after matrix ${matrix[i][50]}")
            }
        }
    }


    fun juanji(h: Array<Double?>, raw: FloatArray): FloatArray {

        val l = h.size + raw.size - 1
        val result = FloatArray(l)
        for (i in 0 until l) {
            var tem = 0.0
            for (j in raw.indices) {
                if ((i - j + 1) >= 0 && (i - j + 1) < h.size) {
                    tem += h[i - j + 1]!! * raw[j]
                }
            }
            result[i] = tem.toFloat()
        }

        val rr = FloatArray(column)

        for ((index, i) in ((column - 1) / 2 until (column - 1) / 2 + column).withIndex()) {
            rr[index] = result[i]
        }
        return rr
    }

    val pi = 4.0 * atan(1.0)
    fun getFirFilterFactor(wn: WindowType, band: Int, fln: Float, fhn: Float): Array<Double?> {
        val n = column
        var n2 = 0
        var mid = 0
        if (n % 2 == 0) {
            n2 = n / 2 - 1
            mid = 1
        } else {
            n2 = n / 2
            mid = 0
        }

        val h = arrayOfNulls<Double>(n + 1)
        val delay = n / 2.0
        var wcl = 2.0 * pi * fln
        var wcl2 = 0.0
        if (band > 3) {
            wcl2 = 2.0 * pi * fhn
        }
        when (band) {
            1 -> {
                for (i in 0..n2) {
                    val s = i - delay
                    h[i] = (sin(wcl * s) / (pi * s)) * window(wn, n + 1, i)
                    h[n - i] = h[i]
                }
                if (mid == 1) h[n / 2] = wcl / pi
            }

            2 -> {
                for (i in 0..n2) {
                    val s = i - delay
                    h[i] = (sin(pi * s) - sin(wcl * s)) / (pi * s)
                    h[i] = h[i]!! * window(wn, n + 1, i)
                    h[n - i] = h[i]
                }
                if (mid == 1) h[n / 2] = 1.0 - wcl / pi
            }

            3 -> {
                for (i in 0..n2) {
                    val s = i - delay
                    h[i] = (sin(wcl2 * s) - sin(wcl * s)) / (pi * s)
                    h[i] = h[i]!! * window(wn, n + 1, i)
                    h[n - i] = h[i]
                }
                if (mid == 1) h[n / 2] = (wcl2 - wcl) / pi
            }

            4 -> {
                for (i in 0..n2) {
                    val s = i - delay
                    h[i] = (sin(wcl * s) + sin(pi * s) - sin(wcl2 * s)) / (pi * s)
                    h[i] = h[i]!! * window(wn, n + 1, i)
                    h[n - i] = h[i]
                }
                if (mid == 1) h[n / 2] = (wcl + pi - wcl2) / pi
            }

        }
        return h
    }


    fun window(wn: WindowType, n: Int, i: Int): Double {
        var w = 1.0
        when (wn) {
            WindowType.juxing -> {
                w = 1.0
            }
            WindowType.tuji -> {
                val k = (n - 2) / 10
                if (i <= k) w = 0.5 * (1.0 - cos(i * pi / (k + 1)))
                if (i > n - k - 2) w = 0.5 * (1.0 - cos((n - i - 1) * pi / (k + 1)))
            }
            WindowType.sanjiao -> {
                w = 1.0 - abs(1.0 - 2 * i / (n - 1.0))
            }
            WindowType.hanning -> {
                w = 0.5 * (1.0 - cos(2 * i * pi / (n - 1)))
            }
            WindowType.haimin -> {
                w = 0.54 - 0.46 * cos(2 * i * pi / (n - 1))
            }
            WindowType.bula -> {
                w = 0.42 - 0.5 * cos(2 * i * pi / (n - 1)) + 0.08 * cos(4 * i * pi / (n - 1))
            }
        }
        return w
    }

    fun sgn(number: Float): Int {
        return when {
            number > 0 -> 1
            number == 0f -> 0
            else -> -1
        }
    }

    //更新最大值最小值
    fun updateMM() {
        max = Float.MIN_VALUE
        min = Float.MAX_VALUE
        matrix.forEach {  //hang
            //存储最大最小值
            val cMax = it.max()!!
            val cMin = it.min()!!
            if (max < cMax) {
                max = cMax
            }
            if (min > cMin) {
                min = cMin
            }
        }

    }

    override fun toString(): String {
        return "max = $max min = $min row $row  col $column 第一个元素: ${matrix[0][0]} 最后一个元素 :${matrix[row - 1][column - 1]}  "
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GPRDataMatrix

        if (row != other.row) return false
        if (column != other.column) return false
        if (!matrix.contentDeepEquals(other.matrix)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + column
        result = 31 * result + matrix.contentDeepHashCode()
        return result
    }

    companion object {
        fun emptyMatrix(): GPRDataMatrix {
            return GPRDataMatrix(0, 0, emptyArray(), 0f, 0f)
        }
    }
}

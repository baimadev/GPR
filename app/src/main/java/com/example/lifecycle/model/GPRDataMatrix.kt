package com.example.lifecycle.model

import com.example.lifecycle.utils.RealDoubleFFT
import com.example.lifecycle.utils.SharedPrefModel
import io.reactivex.Single
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.ln
import kotlin.math.pow

/**
 * 按行存储
 */
data class GPRDataMatrix(var row: Int, var column : Int, var matrix : Array<FloatArray>,var max :Float,var min :Float) {

    val dataInstance = GPRDataManager
    fun clone():GPRDataMatrix{
        val newMatrix = matrix.clone()
        return GPRDataMatrix(row,column, newMatrix,max,min)
    }

    fun copy(gprDataMatrix: GPRDataMatrix){
        row = gprDataMatrix.row
        column = gprDataMatrix.column
        matrix = Array(row){it -> gprDataMatrix.matrix[it].copyOf()}
        max = gprDataMatrix.max
        min = gprDataMatrix.min
    }


    //时间零点计算
    fun timeZero():Float{
        var i = 1
        while (true) {
            if (i >= dataInstance.samples) {
                i = 0
                break
            } else if (matrix[0][i] < matrix[0][i-1] - 400.0) {
                break
            } else {
                i++
            }
        }
        var i2 = i + 1
        while (true) {
            if (i2 >= dataInstance.samples) {
                break
            } else if (matrix[0][i2] > matrix[0][i2-1]) {
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
    fun timeZeroCorrect(offset:Int){
        val newMatrix = Array(row){ FloatArray(column-offset) }
        for(i in 0 until row){
            for(j in offset until column){
                newMatrix[i][j-offset] = matrix[i][j]
            }
        }
        matrix = newMatrix
        column -= offset
        updateMM()
    }

    //DC直流分量校正 垂直滤波
    fun DCFiliter()= Single.fromCallable {
        var num = 0
        var average = 0f
        val startJ = column*0.5.toInt()
        for( i in 0 until row){  //hang
            for( j in startJ until column){  //lie
                average += matrix[i][j]
                num++
            }
        }
        average /= num

        for( i in 0 until row){  //hang
            for( j in 0 until column){  //lie
                matrix[i][j] -= average
            }
        }
        if (max > -min) {
            min = -max
        }
        if (max < -min) {
            max = -min
        }

    }

    fun DCFiliter(trunca:Float) {

        val d4 = 0.9f
        if (max > Math.abs(min)) {
            min = -max
        } else {
            max = -min
        }
        var i3 = 0
        while (i3 < row) {
            var i4 = 0
            while (i4 < column) {
                if (trunca == d4) {
                    matrix[i3][i4] = abs(matrix[i3][i4]) + min
                }
                matrix[i3][i4] = matrix[i3][i4]  * trunca

                if (matrix[i3][i4] > max) {
                    matrix[i3][i4] = max*trunca
                }
                if (matrix[i3][i4] < min) {
                    matrix[i3][i4] = min*trunca
                }
                i4++
            }
            i3++
        }
        max *= trunca
        min *= trunca
    }


    //截断过滤器
    fun TruncationFiliter(truncation : Float)= Single.fromCallable {
            for( i in 0 until row){  //hang
            for( j in 0 until column){  //lie
                matrix[i][j] = atan(matrix[i][j] * 0.001f * truncation)
            }
        }
        updateMM()
    }

    //电位增益滤波器
    fun PotentialGainFilter(truncation : Float) = Single.fromCallable {

        for( i in 0 until row){  //hang
            for( j in 0 until column){  //lie
                val number = matrix[i][j]
                matrix[i][j] = (sgn(number) * (abs(number).toDouble().pow(truncation.toDouble()))).toFloat()
            }
        }
        updateMM()
    }

    //垂直推导滤波
    fun VerticalDerivationFilter(k : Int) = Single.fromCallable {
        
        for( i in 0 until row){  //hang
            for( j in 1 until column){  //lie
                if ( k != 0) {
                   matrix[i][j] = matrix[i][j]-matrix[i][j-1]
                }
                val d3 =  matrix[i][j] / max
                matrix[i][j] = atan(d3 * k)

            }
            matrix[i][0] = 0f
        }
        updateMM()
    }

    //平滑  计算垂直分量 移动平均值
    fun smoothLine(suavitoh:Float) =Single.fromCallable{
        var i: Int
        val i2: Int = row
        val i3: Int = column
        val f: Float =  50.0f
        val dArr =FloatArray(i2)
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
    fun frequency(fouri : Int = 10)=Single.fromCallable{

        var realDoubleFFT: RealDoubleFFT
        var i: Int
        val i3: Int = row
        val i4: Int = column
        var i5 = i4 - fouri
        var realDoubleFFT2 = RealDoubleFFT(fouri)
        val dArr = DoubleArray(fouri)
        val dArr2 = DoubleArray(i4)
        val d = i5.toFloat()
        val d2 = i4.toFloat()
        val d3 = d * 1f / d2
        var i6 = 0
        var d4 = 0.001
        while (i6 < i3) {
            var d5 = d4
            var i7 = 0
            while (i7 < i5) {
                for (i8 in 0 until fouri) {
                    dArr[i8] = matrix[i6][i7 + i8].toDouble()
                }
                realDoubleFFT2.mo6045ft(dArr)
                var i9 = 1
                var d6 = 0.0
                var d7 = 0.0
                while (i9 < fouri) {
                    if (abs(dArr[i9]) > d6) {
                        d6 = dArr[i9]
                        i = i5
                        realDoubleFFT = realDoubleFFT2
                        d7 = ln(i9.toDouble())
                    } else {
                        i = i5
                        realDoubleFFT = realDoubleFFT2
                    }
                    i9++
                    i5 = i
                    realDoubleFFT2 = realDoubleFFT
                }
                val i10 = i5
                val realDoubleFFT3: RealDoubleFFT = realDoubleFFT2
                dArr2[i7] = d7
                if (d5 < d7) {
                    d5 = d7
                }
                i7++
                i5 = i10
                realDoubleFFT2 = realDoubleFFT3
            }
            val i11 = i5
            val realDoubleFFT4: RealDoubleFFT = realDoubleFFT2
            for (i12 in 0 until i4) {
                val d8 = i12.toDouble()
                java.lang.Double.isNaN(d8)
                matrix[i6][i12] = dArr2[(d8 * d3).toInt()].toFloat()
            }
            i6++
            d4 = d5
            i5 = i11
            realDoubleFFT2 = realDoubleFFT4
        }
        updateMM()
    }

    fun sgn(number:Float):Int{
        return when {
            number>0 -> 1
            number == 0f -> 0
            else -> -1
        }
    }

    //更新最大值最小值
    fun updateMM(){
        max = Float.MIN_VALUE
        min = Float.MAX_VALUE
        matrix.forEach{  //hang
            //存储最大最小值
            val cMax = it.max()!!
            val cMin = it.min()!!
            if(max < cMax){
                max = cMax
            }
            if(min > cMin){
                min = cMin
            }
        }

    }

    override fun toString(): String {
        return "max = $max min = $min row $row  col $column 第一个元素: ${matrix[0][0]} 最后一个元素 :${matrix[row-1][column-1]}  "
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

    companion object{
        fun emptyMatrix():GPRDataMatrix{
            return  GPRDataMatrix(0,0, emptyArray(),0f,0f)
        }
    }
}

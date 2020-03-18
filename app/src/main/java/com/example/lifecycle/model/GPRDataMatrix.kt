package com.example.lifecycle.model

import android.util.Log
import com.example.lifecycle.utils.SharedPrefModel
import io.reactivex.Single
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.pow

/**
 * 按行存储
 */
data class GPRDataMatrix(var row: Int, var column : Int, var matrix : Array<FloatArray>,var max :Float,var min :Float) {


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
            if (i >= SharedPrefModel.samples) {
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
            if (i2 >= SharedPrefModel.samples) {
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

        val d= (max - min) / 256f / trunca

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


    //todo 截断过滤器
    fun TruncationFiliter(truncation : Float)= Single.fromCallable {
        Log.d("xia",toString())
            for( i in 0 until row){  //hang
            for( j in 0 until column){  //lie
                if(matrix[i][j] in max-1000 .. max){
                    matrix[i][j] *= truncation
                }
                if(matrix[i][j] in min .. min+1000){
                    matrix[i][j] *= truncation
                }
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

    //垂直推导滤波 需要知道纵向函数表达式
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
        return "max = $max min = $min row $row  col $column 第一个元素: ${matrix[0][0]} 最后一个元素 :${matrix[row-1]!![column-1]}  "
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

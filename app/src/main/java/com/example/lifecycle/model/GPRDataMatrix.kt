package com.example.lifecycle.model

import android.util.Log
import io.reactivex.Single
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.atan

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
        matrix = gprDataMatrix.matrix
        max = gprDataMatrix.max
        min = gprDataMatrix.min
    }

    fun printMatrix(){
        matrix.forEach {

            Log.d("xia","数组长度${it?.size}")
        }
    }

    //DC直流分量校正 垂直滤波
    fun DCFiliter()= Single.fromCallable {
        for( i in 0 until row){  //hang
            val startJ = column*0.75.toInt()
            var average = 0f
            for( j in startJ until column){  //lie
                average += matrix[i]!![j]
            }
            for( j in 0 until column){  //lie
                matrix[i]!![j] -= average
            }
        }
        this
    }

    //截断过滤器
    fun TruncationFiliter(truncation : Float)= Single.fromCallable {
        for( i in 0 until row){  //hang
            for( j in 0 until column){  //lie
                matrix[i]!![j] *= truncation
            }
        }
        //updateMM()
        this
    }

    //电位增益滤波器
    fun PotentialGainFilter(truncation : Float) = Single.fromCallable {

        for( i in 0 until row){  //hang
            for( j in 0 until column){  //lie
                val number = matrix[i]!![j]
                matrix[i]!![j] = (sgn(number) * pow(abs(number).toDouble(), truncation.toDouble())).toFloat()
            }
        }
        updateMM()
        this
    }

    //垂直推导滤波 需要知道纵向函数表达式
    fun VerticalDerivationFilter(k : Int) = Single.fromCallable {


        for( i in 0 until row){  //hang
            for( j in 0 until column){  //lie
                val d = (matrix[i]!![k*j]-matrix[i]!![k*(j-1)])/k
                matrix[i]!![j] = atan(d)
            }
        }
        updateMM()
        this
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
            val cMax = it!!.max()!!
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
        return "row = $row column = $column " +
                "第一个元素: ${matrix[0]!![0]} 最后一个元素 :${matrix[row-1]!![column-1]} "
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

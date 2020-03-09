package com.example.lifecycle.model

import android.util.Log
import io.reactivex.Single

/**
 * 按行存储
 */
data class GPRDataMatrix(var row: Int, var column : Int, var matrix : Array<FloatArray?>,var max :Float,var min :Float) {


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

    //DC直流校正 垂直滤波
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
        this
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
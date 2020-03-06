package com.example.lifecycle.model

import android.util.Log

/**
 * 按行存储
 */
data class GPRDataMatrix(val row: Int, val column : Int, val matrix : Array<FloatArray?>) {


    fun clone():GPRDataMatrix{
        val newMatrix = matrix.clone()
        return GPRDataMatrix(row,column, newMatrix)
    }

    fun printMatrix(){
        matrix.forEach {

            Log.d("xia","数组长度${it?.size}")
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
            return  GPRDataMatrix(0,0, emptyArray())
        }
    }
}
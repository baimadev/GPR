package com.example.lifecycle.model

object GPRDataManager {

    val matrixA:GPRDataMatrix = GPRDataMatrix.emptyMatrix()
    val matrixT:GPRDataMatrix = GPRDataMatrix.emptyMatrix()
    val matrixF:GPRDataMatrix = GPRDataMatrix.emptyMatrix()

    fun initData(matrix:GPRDataMatrix){
        matrixA.copy(matrix)
        matrixT.copy(matrix)
        matrixF.copy(matrix)
    }

}
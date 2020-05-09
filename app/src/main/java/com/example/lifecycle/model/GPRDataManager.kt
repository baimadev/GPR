package com.example.lifecycle.model



object GPRDataManager {

    val matrixA:GPRDataMatrix = GPRDataMatrix.emptyMatrix()
    val matrixT:GPRDataMatrix = GPRDataMatrix.emptyMatrix()
    val matrixF:GPRDataMatrix = GPRDataMatrix.emptyMatrix()

    var frequency = 0f
    var lastTrace = 0
    var col = 0
    var timeWindow =0f
    var distanceInterval =0f
    var defaultTraces = 1500
    var mMidLinePos = defaultTraces / 2
    var dielectric = 6f
    var gprHeight = 2f
    var samples = 0
        set(value) {
            if (value>219){
                gprHeight = 1f
            }
            field = value
        }

    fun initData(matrix:GPRDataMatrix){
        matrix.DCFiliter()
        matrixA.copy(matrix)
        matrixT.copy(matrix)
        matrixF.copy(matrix)
    }

    override fun toString ():String{
        return " \n \n A ${matrixA.toString()}\n F ${matrixF.toString()}\n T $matrixT"
    }

    fun clear(){
        matrixA.copy(GPRDataMatrix.emptyMatrix())
        matrixT.copy(GPRDataMatrix.emptyMatrix())
        matrixF.copy(GPRDataMatrix.emptyMatrix())
        samples = 0
        lastTrace = 0
        col = 0
        timeWindow =0f
        distanceInterval=0f
        defaultTraces = 1500
        mMidLinePos = defaultTraces/2
        dielectric = 6f
        gprHeight = 2f
        frequency = 0f
    }

}
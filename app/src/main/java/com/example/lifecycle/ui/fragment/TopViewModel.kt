package com.example.lifecycle.ui.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.photo.ui.fragment.base.BaseViewModel

class TopViewModel (application: Application):BaseViewModel(application ){
    val layoutShowFlag = MutableLiveData<Boolean>(false)
    val editShowFlag = MutableLiveData<Boolean>(true)
    val editNumber = MutableLiveData<Float>(0f)
    val editMode = MutableLiveData<EditMode>(EditMode.NullMode)

}

fun  MutableLiveData<Float>.add(str: Float){
    val raw = this.value!!
    this.value = raw + str
}

fun  MutableLiveData<Float>.decline(str: Float){
    val raw = this.value!! - str
    if(raw<0f){
        this.value = 0f
    }else{
        this.value = raw
    }

}
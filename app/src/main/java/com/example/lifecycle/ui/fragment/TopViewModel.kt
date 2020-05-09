package com.example.lifecycle.ui.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.photo.ui.fragment.base.BaseViewModel

class TopViewModel (application: Application):BaseViewModel(application ){

    val editNumber = MutableLiveData<Float>(0f)
    val editMode = MutableLiveData<EditMode>(EditMode.NullMode)
    val measureWidth = MutableLiveData<String>("宽度：0 m")
    val measureHeight = MutableLiveData<String>("高度：0 m")
    val smoothNumber = MutableLiveData<Float>(1f)

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
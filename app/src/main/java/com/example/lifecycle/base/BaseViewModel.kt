package com.photo.ui.fragment.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel(application:Application): AndroidViewModel(application) {

    fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

    override fun onCleared() {
        super.onCleared()
    }
}
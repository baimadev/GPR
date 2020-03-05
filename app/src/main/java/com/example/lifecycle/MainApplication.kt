package com.example.lifecycle

import android.app.Application
import android.content.Context
import com.chibatching.kotpref.Kotpref
import com.example.lifecycle.utils.SharedPrefModel
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.autoAndroidModule
import com.github.salomonbrys.kodein.erased.instance

import com.photo.utils.Constants
import indi.yume.tools.fragmentmanager.ThrottleUtil
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

open class MainApplication : Application(), KodeinAware {

    override val kodein: Kodein by Kodein.lazy {

        bind<Context>("AppContext") with singleton { this@MainApplication }

        otherInject()
    }

    open fun Kodein.Builder.otherInject() {

    }

    override fun onCreate() {
        super.onCreate()


        Kotpref.init(this)

        ThrottleUtil.setIsThrottleOpen(true)
        ThrottleUtil.setThrottleTime(300L)
        RxJavaPlugins.setErrorHandler {
                Timber.e(it)
//                toast("UndeliverableException : $it")
        }

    }

    companion object {
        fun getMainApplication(context: Context) = context.applicationContext as MainApplication
    }
}
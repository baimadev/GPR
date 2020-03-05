package com.example.lifecycle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.photo.ui.fragment.base.BaseFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

interface ActivityLifecycleOwner {
    val lifeSubject: Subject<ActivityLifeEvent>

    fun makeState(event: ActivityLifeEvent) = lifeSubject.onNext(event)

    fun destroyLifecycle() = lifeSubject.onComplete()

    fun bindActivityLife(): Observable<ActivityLifeEvent> = lifeSubject

    companion object {
        fun defaultLifeSubject(): Subject<ActivityLifeEvent> =
            PublishSubject.create<ActivityLifeEvent>().toSerialized()
    }
}

interface FragmentLifecycleOwner {
    val lifeSubject: Subject<FragmentLifeEvent>

    fun makeState(event: FragmentLifeEvent) = lifeSubject.onNext(event)

    fun destroyLifecycle() = lifeSubject.onComplete()

    fun bindFragmentLife(): Observable<FragmentLifeEvent> = lifeSubject

    companion object {
        fun defaultLifeSubject(): Subject<FragmentLifeEvent> =
            PublishSubject.create<FragmentLifeEvent>().toSerialized()
    }
}


//<editor-fold defaultstate="collapsed" desc="Activity Life">
sealed class ActivityLifeEvent {
    data class OnCreate(val activity: Activity, val savedInstanceState: Bundle?): ActivityLifeEvent()
    data class OnStart(val activity: Activity): ActivityLifeEvent()
    data class OnResume(val activity: Activity): ActivityLifeEvent()
    data class OnNewIntent(val activity: Activity, val intent: Intent?): ActivityLifeEvent()
    data class OnActivityResult(val activity: Activity, val requestCode: Int,
        val resultCode: Int, val data: Intent?): ActivityLifeEvent()
    data class OnPause(val activity: Activity): ActivityLifeEvent()
    data class OnStop(val activity: Activity): ActivityLifeEvent()
    data class OnDestroy(val activity: Activity): ActivityLifeEvent()
}


//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Fragment Life">
sealed class FragmentLifeEvent {
    data class OnCreate(val fragment: BaseFragment, val savedInstanceState: Bundle?): FragmentLifeEvent()

    data class OnCreateView(val fragment: BaseFragment,
                            val inflater: LayoutInflater,
                            val container: ViewGroup?,
                            val savedInstanceState: Bundle?): FragmentLifeEvent()

    data class OnViewCreated(val fragment: BaseFragment, val view: View?, val savedInstanceState: Bundle?): FragmentLifeEvent()
    data class OnStart(val fragment: BaseFragment) : FragmentLifeEvent()
    data class OnResume(val fragment: BaseFragment) : FragmentLifeEvent()
    data class OnShow(val fragment: BaseFragment, val showMode: OnShowMode): FragmentLifeEvent()
    data class OnHide(val fragment: BaseFragment, val hideMode: OnHideMode): FragmentLifeEvent()
    data class OnDestroy(val fragment: BaseFragment): FragmentLifeEvent()
}

sealed class OnHideMode {
    object OnPause : OnHideMode()
    object OnStartNew : OnHideMode()
    object OnSwitch : OnHideMode()
    object OnStartNewAfterAnim : OnHideMode()

    companion object {
        fun fromMode(mode: Int): OnHideMode =
                when (mode) {
                    indi.yume.tools.fragmentmanager.OnHideMode.ON_PAUSE -> OnPause
                    indi.yume.tools.fragmentmanager.OnHideMode.ON_START_NEW -> OnStartNew
                    indi.yume.tools.fragmentmanager.OnHideMode.ON_START_NEW_AFTER_ANIM -> OnStartNewAfterAnim
                    indi.yume.tools.fragmentmanager.OnHideMode.ON_SWITCH -> OnSwitch
                    else -> OnPause
                }
    }
}

sealed class OnShowMode {
    object OnResume : OnShowMode()
    object OnBack : OnShowMode()
    object OnSwitch : OnShowMode()
    object OnCreate : OnShowMode()
    object OnCreateAfterAnim : OnShowMode()

    companion object {
        fun fromMode(mode: Int): OnShowMode =
                when (mode) {
                    indi.yume.tools.fragmentmanager.OnShowMode.ON_RESUME -> OnResume
                    indi.yume.tools.fragmentmanager.OnShowMode.ON_BACK -> OnBack
                    indi.yume.tools.fragmentmanager.OnShowMode.ON_SWITCH -> OnSwitch
                    indi.yume.tools.fragmentmanager.OnShowMode.ON_CREATE -> OnCreate
                    indi.yume.tools.fragmentmanager.OnShowMode.ON_CREATE_AFTER_ANIM -> OnCreateAfterAnim
                    else -> OnBack
                }
    }
}
//</editor-fold>
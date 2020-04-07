package com.example.lifecycle.base

import android.content.Intent
import android.os.Bundle
import com.example.lifecycle.ActivityLifeEvent
import com.example.lifecycle.ActivityLifecycleOwner
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.AppCompatActivityInjector
import com.photo.utils.BindLife
import indi.yume.tools.fragmentmanager.BaseFragmentManagerActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject


abstract class MainBaseActivity : BaseFragmentManagerActivity(), AppCompatActivityInjector, BindLife,
    ActivityLifecycleOwner {

    //val
    override val injector = KodeinInjector()

    override val compositeDisposable = CompositeDisposable()

    override val lifeSubject: Subject<ActivityLifeEvent> =
        ActivityLifecycleOwner.defaultLifeSubject()

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeInjector()
        super.onCreate(savedInstanceState)

        makeState(ActivityLifeEvent.OnCreate(this, savedInstanceState))
    }

    override fun onStart() {
        super.onStart()
        makeState(ActivityLifeEvent.OnStart(this))
    }

    override fun onResume() {
        super.onResume()
        makeState(ActivityLifeEvent.OnResume(this))
    }

    override fun onPause() {
        super.onPause()
        makeState(ActivityLifeEvent.OnPause(this))
    }

    override fun onStop() {
        super.onStop()
        makeState(ActivityLifeEvent.OnStop(this))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        makeState(ActivityLifeEvent.OnNewIntent(this, intent))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        makeState(ActivityLifeEvent.OnActivityResult(this, requestCode, resultCode, data))
    }

    override fun onDestroy() {
        destroyInjector()
        destroyDisposable()
        super.onDestroy()
        makeState(ActivityLifeEvent.OnDestroy(this))
        destroyLifecycle()
    }

}


package com.photo.ui.fragment.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lifecycle.*
import com.example.lifecycle.utils.hideSoftKeyBoard
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.SupportFragmentInjector
import com.photo.*
import com.photo.utils.BindLife
import indi.yume.tools.fragmentmanager.BaseManagerFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.lang.ClassCastException

abstract class BaseFragment
    : BaseManagerFragment(), SupportFragmentInjector, BindLife, FragmentLifecycleOwner {

    override val injector = KodeinInjector()

    override val compositeDisposable = CompositeDisposable()

    override val lifeSubject: Subject<FragmentLifeEvent> =
        FragmentLifecycleOwner.defaultLifeSubject()

    private val dialogContainer: MutableSet<AlertDialog> = mutableSetOf()

    /**
     * use this fun to listen Activity's lifecycle by the returned Observable
     *
     * bindActivityLife()
     * .ofType<ActivityLifeEvent.OnResume>()
     * .doOnComplete{}
     *
     */

    @Throws(ClassCastException::class)
    fun bindActivityLife(): Observable<ActivityLifeEvent> =
        (context as ActivityLifecycleOwner).bindActivityLife()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeInjector()

        makeState(FragmentLifeEvent.OnCreate(this, savedInstanceState))

        Timber.i("============== onCreated() ==============")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        makeState(FragmentLifeEvent.OnCreateView(this, inflater, container, savedInstanceState))
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeState(FragmentLifeEvent.OnViewCreated(this, view, savedInstanceState))
    }


    override fun onStart() {
        super.onStart()
        makeState(FragmentLifeEvent.OnStart(this))
    }

    override fun onResume() {
        super.onResume()
        makeState(FragmentLifeEvent.OnResume(this))
    }

    override fun onShow(callMode: Int) {
        super.onShow(callMode)
        makeState(FragmentLifeEvent.OnShow(this, OnShowMode.fromMode(callMode)))
    }

    override fun onHide(hideMode: Int) {
        super.onHide(hideMode)
        makeState(FragmentLifeEvent.OnHide(this, OnHideMode.fromMode(hideMode)))
    }

    override fun onStop() {
        super.onStop()
        dialogContainer.forEach { it.dismiss() }
    }

    override fun onDestroy() {
        super.onDestroy()

        makeState(FragmentLifeEvent.OnDestroy(this))
        dialogContainer.apply {
            forEach { it.dismiss() }
            clear()
        }
        destroyLifecycle()
        destroyDisposable()
        destroyInjector()
    }

    override fun finish() {
        activity?.let { hideSoftKeyBoard(it) }
        super.finish()
    }

//    fun lazyContext(): Eval<Context?> = Eval.always { context }

    fun lazyContextUnsafe(): Context = context!!

    //bind dialog's life to fragment
    fun AlertDialog.bindLife():AlertDialog {
        dialogContainer.add(this)
        this.setOnDismissListener { dialogContainer.remove(this) }
        return this
    }

}
package com.photo.ui.fragment.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecycle.R
import com.example.lifecycle.base.CustomToolbar
import com.example.lifecycle.base.MainBaseActivity
import indi.yume.tools.fragmentmanager.BaseFragmentManagerActivity
import indi.yume.tools.fragmentmanager.OnShowMode

abstract class BaseToolbarFragment : BaseFragment() {

    @MenuRes
    open fun provideOptionMenuRes(): Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    protected open fun doForToolbar(toolbar: CustomToolbar) {
        toolbar.run {
            (activity  as? AppCompatActivity)?.setSupportActionBar(this)

            setHasOptionsMenu(provideOptionMenuRes() != -1)

            initListener()
            doOnClickBackButton = doOnClickBackButton ?: { finish() }
            doOnClickCloseButton = doOnClickCloseButton ?: { finish() }
            doOnClickHomeButton = doOnClickCloseButton
                    ?: { (activity as BaseFragmentManagerActivity).clearCurrentStack(true) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val optionMenuRes = provideOptionMenuRes()
        if (optionMenuRes != -1 && isShow) {
            inflater.inflate(optionMenuRes, menu)
        }
    }



    override fun onShow(callMode: Int) {
        super.onShow(callMode)

        (view?.findViewById(R.id.tool_bar) as? CustomToolbar)?.run {
            if (callMode != OnShowMode.ON_CREATE_AFTER_ANIM)
                doForToolbar(this)
        }
    }
}

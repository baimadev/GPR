package com.example.lifecycle.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.lifecycle.R
import com.example.lifecycle.base.BaseActivity
import com.example.lifecycle.base.MainBaseActivity
import com.example.lifecycle.ui.activity.findfileguide.FindFileGuideActivity
import com.example.lifecycle.ui.fragment.TopFragment
import com.photo.utils.Constants

import com.tbruyelle.rxpermissions2.RxPermissions
import indi.yume.tools.fragmentmanager.BaseManagerFragment
import java.io.File
import java.util.concurrent.ThreadLocalRandom

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : MainBaseActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_base)
        switchToStackByTag(Constants.MAIN_FRAGMENT_STACK)
    }


    override fun baseFragmentWithTag(): MutableMap<String, Class<out BaseManagerFragment>> =
        mutableMapOf(Constants.MAIN_FRAGMENT_STACK to TopFragment::class.java)

    override fun fragmentViewId(): Int = R.id.fragment_layout



}
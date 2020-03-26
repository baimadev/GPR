package com.example.lifecycle.base

import android.R
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.lifecycle.ActivityLifeEvent
import com.example.lifecycle.ActivityLifecycleOwner
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.AppCompatActivityInjector
import com.photo.utils.BindLife
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject



abstract class BaseActivity : AppCompatActivity(), AppCompatActivityInjector, BindLife ,
    ActivityLifecycleOwner {

    //val
    override val injector = KodeinInjector()

    override val compositeDisposable = CompositeDisposable()

    override val lifeSubject: Subject<ActivityLifeEvent> = ActivityLifecycleOwner.defaultLifeSubject()

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

    /**
     * 全透状态栏
     */
    protected open fun setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) { //21表示5.0
            val window: Window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.getDecorView().setSystemUiVisibility(
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(Color.TRANSPARENT)
        } else if (Build.VERSION.SDK_INT >= 19) { //19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 半透明状态栏
     */
    protected open fun setHalfTransparent() {
        if (Build.VERSION.SDK_INT >= 21) { //21表示5.0
            val decorView = window.decorView
            val option =
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else if (Build.VERSION.SDK_INT >= 19) { //19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private var contentViewGroup: View? = null

    protected open fun setFitSystemWindow(fitSystemWindow: Boolean) {
        if (contentViewGroup == null) {
            contentViewGroup =
                (findViewById<View>(R.id.content) as ViewGroup).getChildAt(0)
        }
        contentViewGroup!!.fitsSystemWindows = fitSystemWindow
    }

}
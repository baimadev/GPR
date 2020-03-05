package com.example.lifecycle.base

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.Toolbar
import com.example.lifecycle.R
import com.example.lifecycle.base.ToolbarMode.Companion.TOOLBAR_MODE_BACK
import com.example.lifecycle.base.ToolbarMode.Companion.newMode

class CustomToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : Toolbar(context, attrs) {

    var ct_title: String = ""
        set(value) {
            title = value
        }

    var mode: ToolbarMode = NoneMode
        set(value) {
            field = value
            if (value.getIcon() != null)
                setNavigationIcon(value.getIcon()!!)
            else setNavigationIcon(null)
        }

    var doOnClickBackButton: (() -> Unit)? = null
    var doOnClickCloseButton: (() -> Unit)? = null
    var doOnClickHomeButton: (() -> Unit)? = null

    init {
        if (!isInEditMode) {
            title = ""
            setTitleTextColor(Color.WHITE)
            if (attrs != null)
                init(context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar))
        }
    }

    private fun init(tArray: TypedArray) =
            (0..tArray.indexCount)
                    .map { tArray.getIndex(it) }
                    .forEach { setAttr(it, tArray) }

    private fun setAttr(@StyleableRes attrIndex: Int, tArray: TypedArray) {
        when (attrIndex) {
            R.styleable.CustomToolbar_ct_title -> title = tArray.getString(attrIndex)
            R.styleable.CustomToolbar_ct_navigationType -> mode = newMode(tArray.getInt(attrIndex, TOOLBAR_MODE_BACK))
        }
    }

    fun initListener() {
        setNavigationOnClickListener { v -> onClickNavigation() }
    }

    private fun onClickNavigation() {
        when (this.mode) {
            BackMode -> doOnClickBackButton?.invoke()
            CloseMode -> doOnClickCloseButton?.invoke()
            HomeMode -> doOnClickHomeButton?.invoke()
        }
    }
}

sealed class ToolbarMode {
    @DrawableRes
    fun getIcon(): Int? =
            when (this) {
                is NoneMode -> null
                is BackMode -> R.drawable.icon_global_back
                is CloseMode -> R.drawable.icon_global_close
                is HomeMode -> R.drawable.icon_nav_home_tapped
            }

    companion object {
        val TOOLBAR_MODE_NONE = 0
        val TOOLBAR_MODE_BACK = 1
        val TOOLBAR_MODE_CLOSE = 3
        val TOOLBAR_MODE_HOME = 4

        fun newMode(mode: Int): ToolbarMode =
                when (mode) {
                    TOOLBAR_MODE_NONE -> NoneMode
                    TOOLBAR_MODE_BACK -> BackMode
                    TOOLBAR_MODE_CLOSE -> CloseMode
                    TOOLBAR_MODE_HOME -> HomeMode
                    else -> BackMode
                }
    }
}

object NoneMode : ToolbarMode()
object BackMode : ToolbarMode()
object CloseMode : ToolbarMode()
object HomeMode : ToolbarMode()

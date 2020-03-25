package com.example.lifecycle.ui.customview

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.lifecycle.BR
import com.example.lifecycle.R
import com.example.lifecycle.databinding.DialogEditTextBinding
import com.example.lifecycle.utils.ImageHelper
import com.example.lifecycle.utils.SharedPrefModel
import com.example.lifecycle.utils.SharedPrefModel.dielectric
import com.photo.utils.Constants
import io.reactivex.Single
import kotlinx.android.synthetic.main.dialog_color.*
import kotlinx.android.synthetic.main.dialog_color.bt_close
import kotlinx.android.synthetic.main.dialog_color.image_done
import kotlinx.android.synthetic.main.dialog_edit_text.*
import kotlinx.android.synthetic.main.dialog_tfilter.*
import java.io.File
import java.sql.DataTruncation
import java.util.*
class EditDialog2 (mContext: Context):Dialog(mContext, R.style.MyDialog) {

    var onPartInput: ((Float) -> Unit)? = null
    var text :String? = null
    var showInt :Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_text)
        initWindow()

        edit_part.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
        text?.let { tv_hint.text = it }
        showInt?.let { edit_part.setText(showInt.toString()) }

        bt_close.setOnClickListener {
            this.dismiss()
        }

        image_done.setOnClickListener {
            val part = edit_part.text.toString()
            onPartInput?.invoke(part.toFloat())
            this.dismiss()
        }
    }

    private fun initWindow(){
        window!!.setGravity(Gravity.CENTER_VERTICAL)
        val lp = window!!.attributes
        lp.width = CoordinatorLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        lp.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        //点击外部不消失
        setCanceledOnTouchOutside(false)
    }

}
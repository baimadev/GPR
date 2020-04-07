package com.example.lifecycle.ui.customview

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.lifecycle.R
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.utils.SharedPrefModel
import kotlinx.android.synthetic.main.dialog_color.bt_close
import kotlinx.android.synthetic.main.dialog_color.image_done
import kotlinx.android.synthetic.main.dialog_edit_text.*
import kotlinx.android.synthetic.main.dialog_tfilter.*
import java.io.File
import java.sql.DataTruncation
import java.util.*
class EditDialog (mContext: Context):Dialog(mContext, R.style.MyDialog) {

    val dataInstance = GPRDataManager
    var onPartInput: ((Int) -> Unit)? = null
    var onCloseClick: (() -> Unit)? = null
    var file: File? = null
    var text :String? = null
    var showInt :Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_text)
        initWindow()

        val str = context.resources.getString(R.string.part)
        file?.let{
            val part = dataInstance.lastTrace/ dataInstance.defaultTraces
            val size = (it.length()/(1024f*1024f)).toInt()
            val string = String.format(str,size,part)
            tv_hint.text = string
        }
        edit_part.setText(1.toString())

        text?.let { tv_hint.text = it }
        showInt?.let { edit_part.setText(showInt.toString()) }

        bt_close.setOnClickListener {
            this.dismiss()
            onCloseClick?.invoke()
        }

        image_done.setOnClickListener {
            val part = edit_part.text.toString()
            if(part.isNotEmpty() && (part.toInt() in 1..dataInstance.lastTrace/ SharedPrefModel.defaultTraces)){
                onPartInput?.invoke(part.toInt())
            }else{
                Toast.makeText(context,"请输入正确范围的值！", Toast.LENGTH_SHORT).show()
            }

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
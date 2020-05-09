package com.example.lifecycle.ui.customview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.lifecycle.R
import com.example.lifecycle.model.GPRDataManager
import kotlinx.android.synthetic.main.dialog_signal_amp.*



class SignalAmplificationDialog (mContext: Context): Dialog(mContext, R.style.MyDialog){

    var onTruncation: (() -> Unit)? = null
    var onPotential: (() -> Unit)? = null
    var onAtan: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_signal_amp)
        initWindow()

        radio_group.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.sb_bhd ->{ onTruncation?.invoke() }
                R.id.sb_ld ->{onPotential?.invoke()}
                R.id.sb_sd ->{onAtan?.invoke()}
            }
        }

        image_done.setOnClickListener {
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
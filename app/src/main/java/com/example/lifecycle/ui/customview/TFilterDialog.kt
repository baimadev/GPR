package com.example.lifecycle.ui.customview

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.SeekBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.lifecycle.R
import com.example.lifecycle.utils.ImageHelper
import com.example.lifecycle.utils.SharedPrefModel
import io.reactivex.Single
import kotlinx.android.synthetic.main.dialog_color.*
import kotlinx.android.synthetic.main.dialog_color.bt_close
import kotlinx.android.synthetic.main.dialog_color.image_done
import kotlinx.android.synthetic.main.dialog_tfilter.*
import java.sql.DataTruncation
import java.util.*
class TFilterDialog (mContext: Context, val layoutId :Int):Dialog(mContext, R.style.MyDialog) {

    lateinit var truncationSinle : Single<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.setGravity(Gravity.CENTER_VERTICAL)
        setContentView(layoutId)
        val lp = window!!.attributes
        //   val display = (context as Activity).windowManager.defaultDisplay
        //宽度
        lp.width = CoordinatorLayout.LayoutParams.WRAP_CONTENT
        lp.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
        lp.y = 0
        lp.y = 0

        // lp.gravity = Gravity.CENTER
        window!!.attributes = lp
        //点击外部不消失
        setCanceledOnTouchOutside(false)
        bt_close.setOnClickListener {
            this.dismiss()
        }

        //截断值
        sb_truncation.let {
            it.progress = SharedPrefModel.mTruncation
            it.max = 100
            it.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    SharedPrefModel.mTruncation = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })
        }

        image_done.setOnClickListener {
            truncationSinle = Single.just(SharedPrefModel.mTruncation)
            this.dismiss()
        }
    }


}
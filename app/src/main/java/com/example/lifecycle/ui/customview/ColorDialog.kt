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
import kotlinx.android.synthetic.main.dialog_color.*

class ColorDialog (mContext: Context, val layoutId :Int,val gprImageView: GPRImageView):Dialog(mContext, R.style.MyDialog) {

    companion object{
        const val MAX_VALUE = 255
        const val MID_VALUE = 127
    }
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

        //seekbar
        //饱和度
        sb_bhd.let {
            it.max = MAX_VALUE
            it.progress = SharedPrefModel.mSaturationPos
            it.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    SharedPrefModel.mSaturation =progress * 1.0f/ MAX_VALUE
                    SharedPrefModel.mSaturationPos =progress
                    setRawGprBitmapHL()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }
        //亮度
        sb_ld.let {
            it.max = MAX_VALUE
            it.progress = SharedPrefModel.mLumPos
            it.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    SharedPrefModel.mLum = progress * 1.0f / MAX_VALUE
                    SharedPrefModel.mLumPos = progress
                    setRawGprBitmapHL()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })
        }

        //色相
        sb_sd.let {
            it.max = 11
            it.progress = SharedPrefModel.mHuePos
            it.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    SharedPrefModel.mHuePos = progress
                    gprImageView.drawBitmap(progress)
                    gprImageView.drawGprBitmapOnMain()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })
        }

        image_done.setOnClickListener {
            this.dismiss()
        }
    }

    fun setRawGprBitmapHL(){
        val bitmap = ImageHelper.handleImageEffec(gprImageView.rawGprBitmap!!, SharedPrefModel.mSaturation, SharedPrefModel.mLum)
        gprImageView.showGprBitmap = bitmap
        gprImageView.drawGprBitmapOnMain()
    }
}
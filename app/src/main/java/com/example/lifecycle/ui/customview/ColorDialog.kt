package com.example.lifecycle.ui.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.SeekBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.lifecycle.GPRImageView
import com.example.lifecycle.R
import com.example.lifecycle.utils.ImageHelper
import com.example.lifecycle.utils.SharedPrefModel
import kotlinx.android.synthetic.main.dialog_color.*
import java.util.*


class ColorDialog (mContext: Context, val layoutId :Int,val gprImageView: GPRImageView):Dialog(mContext, R.style.MyDialog) {


    private var mImageView: ImageView? = null

    private val mBitmap: Bitmap? = null

    companion object{
        const val MAX_VALUE = 255
        const val MID_VALUE = 127
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window!!.setGravity(Gravity.CENTER)
        setContentView(layoutId)

        val lp = window!!.attributes
        //宽度
        lp.width = CoordinatorLayout.LayoutParams.MATCH_PARENT
        lp.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        window!!.attributes = lp
        //点击外部不消失
        setCanceledOnTouchOutside(false)
        bt_close.setOnClickListener {
            this.dismiss()
        }
        //spinner
        val colorArray = listOf<Int>(R.drawable.ic_arrow_drop_down_black_24dp,R.drawable.brvah_sample_footer_loading_progress)
        val adapter = SpinnerAdapter(context,colorArray,R.layout.item_spinner)
        //sp_sd.setAdapter(adapter)

        val dataset: List<String> =
            LinkedList(Arrays.asList("One", "Two", "Three", "Four", "Five"))
        sp_sd.attachDataSource(dataset)

        //seekbar
        //饱和度
        sb_bhd.let {
            it.max = MAX_VALUE
            it.progress = SharedPrefModel.mSaturationPos
            it.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    SharedPrefModel.mSaturation =progress * 1.0f/ MID_VALUE
                    SharedPrefModel.mSaturationPos =progress
                    setImageview()
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
                    SharedPrefModel.mLum = progress * 1.0f / MID_VALUE
                    SharedPrefModel.mLumPos = progress
                    setImageview()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })
        }

        //色调
        sb_sd.let {
            it.max = MAX_VALUE
            it.progress = SharedPrefModel.mHuePos
            it.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    SharedPrefModel.mHue = progress * 1.0f / MID_VALUE
                    SharedPrefModel.mHuePos = progress 
                    setImageview()
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

    fun setImageview(){
        val bitmap = ImageHelper.handleImageEffec(gprImageView.getCurrentBitmap(), SharedPrefModel.mHue, SharedPrefModel.mSaturation, SharedPrefModel.mLum)
        gprImageView.setImageBitmap(bitmap)
    }


}
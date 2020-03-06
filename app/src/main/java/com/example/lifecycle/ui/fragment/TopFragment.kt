package com.example.lifecycle.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.example.lifecycle.GPRImageView
import com.example.lifecycle.R
import com.example.lifecycle.base.CloseMode
import com.example.lifecycle.base.CustomToolbar
import com.example.lifecycle.databinding.FragmentTopBinding
import com.example.lifecycle.ui.activity.SplashActivity
import com.example.lifecycle.ui.customview.ColorDialog
import com.example.lifecycle.utils.FileUtil
import com.jakewharton.rxbinding2.view.RxView
import com.photo.ui.fragment.base.BindingFragment
import com.photo.utils.Constants
import com.photo.utils.netProgressDialog
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_top.*
import java.io.File


class TopFragment : BindingFragment<FragmentTopBinding, TopViewModel>(TopViewModel::class.java, R.layout.fragment_top){

    lateinit var gprImage:GPRImageView

    override fun initView() {
        gprImage = matrix
        gprImage.let {
            gprImage = matrix
        }
//        tool_bar.let {
//            it.doOnClickCloseButton = {
//                finish()
//                startActivity(Intent(context,SplashActivity::class.java))
//            }
//        }

        RxView.clicks(binding.imageClose)
            .doOnNext {
                finish()
                startActivity(Intent(context,SplashActivity::class.java))
            }
            .bindLife()


        RxView.clicks(binding.imageColor)
            .doOnNext {
                val dialog = ColorDialog(context!!,R.layout.dialog_color,gprImage)
                Log.d("xia","click")
                dialog.show()
            }
            .bindLife()

    }

    override fun initData() {
        val rd3File = File(Constants.RD3)

        Single.just(FileUtil.readFileToMatrix2(rd3File,1000))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .netProgressDialog(context!!)
            .doOnSuccess {
                // set the default image display type
                gprImage.setGPRData(it)
                gprImage.invalidate()

            }
            .bindLife()

    }
}
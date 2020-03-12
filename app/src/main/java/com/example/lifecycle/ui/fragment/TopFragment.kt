package com.example.lifecycle.ui.fragment

import android.content.Intent
import android.util.Log
import com.example.lifecycle.ui.customview.GPRImageView
import com.example.lifecycle.R
import com.example.lifecycle.databinding.FragmentTopBinding
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.ui.activity.SplashActivity
import com.example.lifecycle.ui.customview.ColorDialog
import com.example.lifecycle.ui.customview.TFilterDialog
import com.example.lifecycle.utils.ColorUtils
import com.example.lifecycle.utils.FileUtil
import com.jakewharton.rxbinding2.view.RxView
import com.photo.ui.fragment.base.BindingFragment
import com.photo.utils.Constants
import com.photo.utils.netProgressDialog
import com.photo.utils.switchThread
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_top.*
import java.io.File


class TopFragment : BindingFragment<FragmentTopBinding, TopViewModel>(
    TopViewModel::class.java,
    R.layout.fragment_top
) {

    lateinit var gprImage: GPRImageView
    val gprDataManager = GPRDataManager

    override fun initView() {
        gprImage = matrix
        gprImage.let {
            gprImage = matrix
        }

        RxView.clicks(binding.imageClose)
            .doOnNext {
                compositeDisposable.clear()
                finish()
                startActivity(Intent(context, SplashActivity::class.java))
            }
            .bindLife()


        //调色板
        RxView.clicks(binding.imageColor)
            .doOnNext {
                val dialog = ColorDialog(context!!, R.layout.dialog_color, gprImage)
                dialog.show()
            }
            .bindLife()
        //DC
        RxView.clicks(binding.imageCrop)
            .doOnNext {
                gprDataManager
                    .matrixA
                    .DCFiliter()
                    .switchThread(subscribeOn = Schedulers.computation())
                    .netProgressDialog(context!!)
                    .doOnSuccess {
                        gprImage.updateData(it)
                            //.switchThread()
                            .bindLife()
                    }
                    .bindLife()
            }
            .bindLife()

//        //截断滤波
//        RxView.clicks(binding.imageTf)
//            .doOnNext {
//                val dialog = TFilterDialog(context!!, R.layout.dialog_tfilter)
//                dialog.show()
//                dialog.onProgressChange = { it: Int ->
//                    gprDataManager
//                        .matrixA
//                        .TruncationFiliter(it / 100f)
//                        .switchThread(subscribeOn = Schedulers.computation())
//                        .netProgressDialog(context!!)
//                        .doOnSuccess {
//                            gprImage.updateData(it)
//                                //.switchThread()
//                                .bindLife()
//                        }
//                        .bindLife()
//                }
//
//            }
//            .bindLife()

        //电位增益滤波
        RxView.clicks(binding.imageTf)
            .doOnNext {
                val dialog = TFilterDialog(context!!, R.layout.dialog_tfilter)
                dialog.show()
                dialog.onProgressChange = { it: Int ->
                    gprDataManager
                        .matrixA
                        .PotentialGainFilter(it .toFloat())
                        .switchThread(subscribeOn = Schedulers.computation())
                        .netProgressDialog(context!!)
                        .doOnSuccess {
                            gprImage.updateData(it)
                                //.switchThread()
                                .bindLife()
                        }
                        .bindLife()
                }

            }
            .bindLife()



    }

    override fun initData() {
        Single.just(ColorUtils.initColoracion())
            .switchThread()
            .doOnSuccess {
                val d =GPRDataManager.matrixT
                d.printMatrix()
                // set the default image display type
                gprImage.initImageBitmap(GPRDataManager.matrixT)
                    .switchThread()
                    .netProgressDialog(context!!)
                    .doOnSuccess {
                       gprImage.invalidate()
                    }
                    .bindLife()
            }
            .bindLife()
    }
}
package com.example.lifecycle.ui.fragment

import android.content.Intent
import android.util.Log
import android.view.ViewTreeObserver
import com.example.lifecycle.ui.customview.GPRImageView
import com.example.lifecycle.R
import com.example.lifecycle.databinding.FragmentTopBinding
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.model.GPRDataMatrix
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
import timber.log.Timber
import java.io.File


class TopFragment : BindingFragment<FragmentTopBinding, TopViewModel>(
    TopViewModel::class.java,
    R.layout.fragment_top
) {

    lateinit var gprImage: GPRImageView
    val gprDataManager = GPRDataManager

    override fun initView() {
        gprImage = matrix
        binding.lifecycleOwner = this
        binding.data = viewModel

        RxView.clicks(binding.imageClose)
            .doOnNext {
                compositeDisposable.clear()
                finish()
                startActivity(Intent(context, SplashActivity::class.java))
            }
            .bindLife()

        line_image.onTranslate = {
            energy_image.updateData(it)
        }

        //
        viewModel.layoutShowFlag.observe {

        }
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
                filter {
                    it.DCFiliter()
                }
            }
            .bindLife()

        //保存
        RxView.clicks(binding.imageSave)
            .doOnNext {
                saveData()
            }
            .bindLife()

        //撤销
        RxView.clicks(binding.imageRevoke)
            .doOnNext {
                revoke()
            }
            .bindLife()

        //时间零点
        RxView.clicks(time_zero)
            .doOnNext {
                val offset =GPRDataManager.matrixT.timeZero()
                viewModel.editNumber.value = offset
                viewModel.layoutShowFlag.value =true
                viewModel.editMode.value = EditMode.TimeZero
            }
            .bindLife()

        //增加
        RxView.clicks(image_enhance)
            .doOnNext {
                when(viewModel.editMode.value){
                    EditMode.TimeZero -> {
                        viewModel.editNumber.add(1f)
                        timeZeroCorrect(viewModel.editNumber.value!!.toInt())

                    }
                    EditMode.DCFilter -> {

                    }
                    else -> {

                    }
                }
            }
            .bindLife()

        //减少
        RxView.clicks(image_decline)
            .doOnNext {
                when(viewModel.editMode.value){
                    EditMode.TimeZero -> {
                        viewModel.editNumber.decline(1f)
                        timeZeroCorrect(viewModel.editNumber.value!!.toInt())
                    }
                    EditMode.DCFilter -> {

                    }
                    else -> {

                    }
                }
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
                        .PotentialGainFilter(it.toFloat())
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

    fun saveData(){
        GPRDataManager.matrixF.copy(GPRDataManager.matrixT)
        updateView(GPRDataManager.matrixF)
    }

    fun revoke() {
        GPRDataManager.run {
            updateView(matrixF) {matrixT.copy(matrixF)}
        }
    }

    fun filter(filter:(GPRDataMatrix) -> Single<Unit>){
        filter.invoke(GPRDataManager.matrixT)
            .switchThread()
            .netProgressDialog(context!!)
            .doOnSuccess {
                updateView(GPRDataManager.matrixT)
            }
            .bindLife()

    }

    override fun initData() {

        gprImage.onDrawObserver = {
            if (gprImage.initFlag == 0){
                gprImage.initFlag+=1
                gprImage.initImageBitmap(GPRDataManager.matrixF)
                    .switchThread()
                    .netProgressDialog(context!!)
                    .subscribe({
                        gprImage.setImageBitmap(it)

                    }, {
                        Timber.e(it)
                    })
            }
        }
    }

    //每一次操作 应该都是上一次数据
    fun timeZeroCorrect(offset:Int){
        GPRDataManager.matrixT.copy(GPRDataManager.matrixF)
        Single.just(GPRDataManager.matrixT.timeZeroCorrect(offset))
            .switchThread()
            .netProgressDialog(context!!)
            .doOnSuccess {
                updateView(GPRDataManager.matrixT)
            }
            .bindLife()
    }

    fun updateView(matrix:GPRDataMatrix,operation:(()->Unit) ? = null){
        gprImage.updateData(matrix)
            .switchThread()
            .netProgressDialog(context!!)
            .doOnSuccess {
                energy_image.updateData(data = matrix)
                operation?.invoke()
            }
            .bindLife()
    }
}
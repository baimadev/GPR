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
                viewModel.layoutShowFlag.value =true
                viewModel.editShowFlag.value =true
                viewModel.editNumber.value = 1f
                viewModel.editMode.value = EditMode.DCFilter
                filter {
                    it.DCFiliter()
                }
            }
            .bindLife()

        //时间零点
        RxView.clicks(time_zero)
            .doOnNext {
                val offset =GPRDataManager.matrixT.timeZero()
                viewModel.editNumber.value = offset
                viewModel.layoutShowFlag.value =true
                viewModel.editShowFlag.value =true
                viewModel.editMode.value = EditMode.TimeZero
            }
            .bindLife()


        //截断滤波
        RxView.clicks(binding.imageTf)
            .doOnNext {
                viewModel.editNumber.value = 1f
                viewModel.layoutShowFlag.value =true
                viewModel.editShowFlag.value =true
                viewModel.editMode.value = EditMode.Truncation
            }
            .bindLife()

        //电位增益滤波
        RxView.clicks(binding.imagePotential)
            .doOnNext {
                viewModel.editNumber.value = 1f
                viewModel.layoutShowFlag.value =true
                viewModel.editShowFlag.value =true
                viewModel.editMode.value = EditMode.Potential
            }
            .bindLife()

        //垂直推导
        RxView.clicks(binding.imageDerivation)
            .doOnNext {
                viewModel.editNumber.value = 1f
                viewModel.layoutShowFlag.value =true
                viewModel.editShowFlag.value =true
                viewModel.editMode.value = EditMode.Derivation
            }
            .bindLife()
        //平滑
        RxView.clicks(binding.imageBackground)
            .doOnNext {
                viewModel.editNumber.value = 1f
                viewModel.layoutShowFlag.value =true
                viewModel.editShowFlag.value =true
                viewModel.editMode.value = EditMode.Background

            }
            .bindLife()

        //频率域
        RxView.clicks(binding.imageFrequency)
            .doOnNext {
                viewModel.editNumber.value = 1f
                viewModel.layoutShowFlag.value =true
                viewModel.editShowFlag.value =true
                viewModel.editMode.value = EditMode.Frequency
                //todo
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
                    EditMode.DCFilter ->{
                        viewModel.editNumber.let {
                            it.value = String.format("%.2f",it.value!! *2f).toFloat()
                            dcFiliter(it.value!!)
                        }
                    }
                    EditMode.Truncation ->{
                        viewModel.editNumber.let {
                            val t = it.value!! +  0.25f
                            if(t>1f){
                                it.value = 1f
                            }else{
                                it.value = t
                            }
                            filter { matrix ->
                                matrix.TruncationFiliter(it.value!!)
                            }
                        }
                    }
                    EditMode.Potential ->{
                        viewModel.editNumber.let {
                            it.value = String.format("%.2f",it.value!! *0.75f).toFloat()
                            filter { matrix ->
                                matrix.PotentialGainFilter(it.value!!)
                            }
                        }
                    }
                    EditMode.Derivation ->{
                        viewModel.editNumber.let {
                            it.add(1f)
                            filter { matrix ->
                                matrix.VerticalDerivationFilter(it.value!!.toInt())
                            }
                        }
                    }
                    EditMode.Background -> {
                        viewModel.editNumber.let {
                            it.value = String.format("%.2f",it.value!! *2f).toFloat()
                            filter { matrix ->
                                matrix.smoothLine(it.value!!)
                            }
                        }

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
                        viewModel.editNumber.decline(5f)
                        timeZeroCorrect(viewModel.editNumber.value!!.toInt())
                    }

                    EditMode.DCFilter ->{
                        viewModel.editNumber.let {
                            val t = String.format("%.2f",it.value!! *0.75f).toFloat()
                            if(t<0.9f){
                                it.value = 0.9f
                            }else{
                                it.value = t
                            }
                            dcFiliter(it.value!!)
                        }
                    }

                    EditMode.Truncation ->{
                        viewModel.editNumber.let {
                            val t = it.value!! -  0.25f
                            if(t<0f){
                                it.value = 0f
                            }else{
                                it.value = t
                            }
                            filter { matrix ->
                                matrix.TruncationFiliter(it.value!!)
                            }
                        }
                    }

                    EditMode.Potential ->{
                        viewModel.editNumber.let {
                            it.value = String.format("%.2f",it.value!! *2f).toFloat()
                            filter { matrix ->
                                matrix.PotentialGainFilter(it.value!!)
                            }
                        }
                    }

                    EditMode.Derivation ->{
                        viewModel.editNumber.let {
                            it.decline(1f)
                            filter { matrix ->
                                matrix.VerticalDerivationFilter(it.value!!.toInt())
                            }
                        }
                    }

                    EditMode.Background -> {
                        viewModel.editNumber.let {
                            it.value = String.format("%.2f",it.value!! *0.5f).toFloat()
                            if(it.value!!<1f){
                                it.value = 1f
                            }
                            filter { matrix ->
                                matrix.smoothLine(it.value!!)
                            }
                        }

                    }
                    else -> {

                    }
                }
            }
            .bindLife()

        //保存
        RxView.clicks(binding.imageSave)
            .doOnNext {
                viewModel.layoutShowFlag.value = false
                saveData()
            }
            .bindLife()

        //撤销
        RxView.clicks(binding.imageRevoke)
            .doOnNext {
                revoke()
            }
            .bindLife()

    }

    fun saveData(){
        GPRDataManager.matrixF.copy(GPRDataManager.matrixT)
        updateView(GPRDataManager.matrixF)
    }

    fun revoke() {
        GPRDataManager.run {
            updateView(matrixF) {
                matrixT.copy(matrixF)
            }
        }
    }

    fun filter(filter:(GPRDataMatrix) -> Single<Unit>){
        GPRDataManager.matrixT.copy(GPRDataManager.matrixF)
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

    fun timeZeroCorrect(offset:Int){
        //每一次操作 应该都是上一次数据
        GPRDataManager.matrixT.copy(GPRDataManager.matrixF)
        Single.just(GPRDataManager.matrixT.timeZeroCorrect(offset))
            .switchThread()
            .netProgressDialog(context!!)
            .doOnSuccess {
                updateView(GPRDataManager.matrixT)
            }
            .bindLife()
    }

    fun dcFiliter(offset:Float){
        //每一次操作 应该都是上一次数据
        GPRDataManager.matrixT.copy(GPRDataManager.matrixF)
        Single.just(GPRDataManager.matrixT.DCFiliter(offset))
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
package com.example.lifecycle.ui.fragment

import android.app.AlertDialog
import android.util.Log
import com.example.lifecycle.ui.customview.GPRImageView
import com.example.lifecycle.R
import com.example.lifecycle.databinding.FragmentTopBinding
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.model.GPRDataMatrix
import com.example.lifecycle.ui.customview.ColorDialog
import com.example.lifecycle.ui.customview.FrequencyFilterDialog
import com.example.lifecycle.ui.customview.SignalAmplificationDialog
import com.example.lifecycle.utils.fft
import com.example.lifecycle.utils.iFFT
import com.jakewharton.rxbinding2.view.RxView
import com.photo.ui.fragment.base.BindingFragment
import com.photo.utils.netProgressDialog
import com.photo.utils.switchThread
import io.reactivex.Single
import kotlinx.android.synthetic.main.fragment_top.*
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.sqrt


class TopFragment : BindingFragment<FragmentTopBinding, TopViewModel>(
    TopViewModel::class.java,
    R.layout.fragment_top
) {

    val dataInstance = GPRDataManager
    lateinit var gprImage: GPRImageView

    override fun initView() {
        gprImage = matrix
        binding.lifecycleOwner = this
        binding.data = viewModel

        //关闭
        RxView.clicks(binding.imageClose)
            .doOnNext {
                onClose()
            }
            .bindLife()

        line_image.onTranslate = {
            energy_image.updateData(it)
        }


        //调色板
        RxView.clicks(binding.imageColor)
            .doOnNext {
                revoke()
                val dialog = ColorDialog(context!!, R.layout.dialog_color, gprImage)
                dialog.show()
            }
            .bindLife()

        //重置
        RxView.clicks(binding.imageReset)
            .doOnNext {
                viewModel.editMode.value = EditMode.NullMode
                GPRDataManager.run {
                    matrixT.copy(matrixA)
                    matrixF.copy(matrixA)
                    updateView(matrixF)
                }
            }
            .bindLife()

        //信号增益
        RxView.clicks(binding.signalAmplificatiion)
            .doOnNext {
                val dialog = SignalAmplificationDialog(context!!)
                    .apply {
                        //截断增益滤波
                        onTruncation = {
                            revoke()
                            viewModel.editNumber.value = 0.1f
                            viewModel.editMode.value = EditMode.Truncation
                        }
                        //电位增益滤波
                        onPotential = {
                            revoke()
                            viewModel.editNumber.value = 1f
                            viewModel.editMode.value = EditMode.Potential
                        }
                        //反正切增益滤波
                        onAtan = {
                            revoke()
                            viewModel.editNumber.value = 0.1f
                            viewModel.editMode.value = EditMode.Atan
                        }
                    }
                dialog.show()
            }
            .bindLife()
        //DC
//        RxView.clicks(binding.imageCrop)
//            .doOnNext {
//                revoke()
//                viewModel.editNumber.value = 1f
//                viewModel.editMode.value = EditMode.DCFilter
//                GPRDataManager.matrixT.DCFiliter()
//                    .switchThread()
//                    .netProgressDialog(context!!)
//                    .bindLife()
//            }
//            .bindLife()

        //时间零点
        RxView.clicks(time_zero)
            .doOnNext {
                revoke()
                val offset = GPRDataManager.matrixT.timeZero()
                viewModel.editNumber.value = offset
                viewModel.editMode.value = EditMode.TimeZero
            }
            .bindLife()


        //垂直推导
        RxView.clicks(binding.imageDerivation)
            .doOnNext {
                revoke()
                viewModel.editNumber.value = 1f
                viewModel.editMode.value = EditMode.Derivation
            }
            .bindLife()
        //平滑
        RxView.clicks(binding.imageBackground)
            .doOnNext {
                revoke()
                viewModel.editNumber.value = 0.1f
                viewModel.editMode.value = EditMode.Background

            }
            .bindLife()

        //频率域
        RxView.clicks(binding.imageFrequency)
            .doOnNext {
                revoke()
                viewModel.editNumber.value = 10f
                viewModel.editMode.value = EditMode.Frequency
//                filter {
//                    it.frequency()
//                }
                val dialog = FrequencyFilterDialog(context!!)
                dialog.aContext = context!!
                dialog.onEnsure = { lx,clx,lBand,hBnad ->
                    Log.e("xia","invoke")
                    Log.e("xia","$lx $clx $lBand")
                    filter {
                        it.firFiliter(clx,lx,lBand,hBnad)
                    }
                }
                dialog.show()
            }
            .bindLife()

        //测量
        RxView.clicks(binding.imageMeasure)
            .doOnNext {
                revoke()
                viewModel.editMode.value = EditMode.Measure
            }
            .bindLife()

        //确定测量
        RxView.clicks(image_ensure)
            .doOnNext {
                val verticalLeft = binding.leftVerticalLine.trace
                val verticalRight = binding.rightVerticalLine.trace
                val horizontalLeft = binding.leftHorizontalLine.trace
                val horizontalRight = binding.rightHorizontalLine.trace
                measure(verticalLeft, verticalRight, horizontalLeft, horizontalRight)
            }
            .bindLife()

        //增加
        RxView.clicks(image_enhance)
            .doOnNext {
                when (viewModel.editMode.value) {
                    EditMode.TimeZero -> {
                        viewModel.editNumber.add(1f)
                        timeZeroCorrect(viewModel.editNumber.value!!.toInt())
                    }
//                    EditMode.DCFilter -> {
//                        viewModel.editNumber.let {
//                            it.value = String.format("%.2f", it.value!! * 2f).toFloat()
//                            dcFiliter(it.value!!)
//                        }
//                    }
                    EditMode.Atan -> {
                        viewModel.editNumber.let {
                            it.value = String.format("%.2f", it.value!! * 2f).toFloat()
                            filter { matrix ->
                                matrix.AtanFiliter(it.value!!)
                            }
                        }
                    }

                    EditMode.Truncation -> {
                        viewModel.editNumber.let {
                            it.value = String.format("%.2f", it.value!! * 2f).toFloat()
                            if (it.value!!>1f){it.value = 1f}
                            filter { matrix ->
                                matrix.tfFiliter(it.value!!)
                            }
                        }
                    }

                    EditMode.Potential -> {
                        viewModel.editNumber.let {
                            it.value = String.format("%.2f", it.value!! * 0.75f).toFloat()
                            filter { matrix ->
                                matrix.PotentialGainFilter(it.value!!)
                            }
                        }
                    }
                    EditMode.Derivation -> {
                        viewModel.editNumber.let {
                            it.add(1f)
                            filter { matrix ->
                                matrix.VerticalDerivationFilter(it.value!!.toInt())
                            }
                        }
                    }
                    EditMode.Background -> {
                        val cur = viewModel.editNumber.value!!
                        if(cur==1f) return@doOnNext
                        viewModel.smoothNumber.let {
                            it.value = String.format("%.2f", it.value!! * 2f).toFloat()
                            filter { matrix ->
                                matrix.smoothLine(it.value!!)
                            }
                            if(cur+0.1f>1f){
                                viewModel.editNumber.value = 1f
                            }else{
                                viewModel.editNumber.value = String.format("%.2f", cur+0.1f).toFloat()
                            }
                        }

                    }
                    EditMode.Frequency -> {
                        viewModel.editNumber.let {
                            it.add(4f)
                            filter { matrix ->
                                matrix.frequency(it.value!!.toInt())
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
                when (viewModel.editMode.value) {
                    EditMode.TimeZero -> {
                        viewModel.editNumber.decline(5f)
                        timeZeroCorrect(viewModel.editNumber.value!!.toInt())
                    }

//                    EditMode.DCFilter -> {
//                        viewModel.editNumber.let {
//                            val t = String.format("%.2f", it.value!! * 0.75f).toFloat()
//                            if (t < 0.9f) {
//                                it.value = 0.9f
//                            } else {
//                                it.value = t
//                            }
//                            dcFiliter(it.value!!)
//                        }
//                    }

                    EditMode.Atan -> {
                        viewModel.editNumber.let {
                            val f = String.format("%.2f", it.value!! * 0.5f).toFloat()
                            if (f < 0.01f) {
                                it.value = 0.01f
                            } else {
                                it.value = f
                            }
                            filter { matrix ->
                                matrix.AtanFiliter(it.value!!)
                            }
                        }
                    }

                    EditMode.Truncation -> {
                        viewModel.editNumber.let {
                            val f = String.format("%.2f", it.value!! * 0.5f).toFloat()
                            if (f < 0.01f) {
                                it.value = 0.01f
                            } else {
                                it.value = f
                            }
                            filter { matrix ->
                                matrix.tfFiliter(it.value!!)
                            }
                        }
                    }

                    EditMode.Potential -> {
                        viewModel.editNumber.let {
                            it.value = String.format("%.2f", it.value!! * 2f).toFloat()
                            filter { matrix ->
                                matrix.PotentialGainFilter(it.value!!)
                            }
                        }
                    }

                    EditMode.Derivation -> {
                        viewModel.editNumber.let {
                            it.decline(1f)
                            filter { matrix ->
                                matrix.VerticalDerivationFilter(it.value!!.toInt())
                            }
                        }
                    }

                    EditMode.Background -> {
                        viewModel.smoothNumber.let {
                            it.value = String.format("%.2f", it.value!! * 0.5f).toFloat()
                            if (it.value!! < 1f) {
                                it.value = 1f
                            }
                            filter { matrix ->
                                matrix.smoothLine(it.value!!)
                            }
                            val cur = viewModel.editNumber.value!!
                            if(cur-0.1f<=0f){
                                viewModel.editNumber.value = 0f
                            }else{
                                viewModel.editNumber.value = String.format("%.2f", cur-0.1f).toFloat()
                            }
                        }

                    }

                    EditMode.Frequency -> {
                        viewModel.editNumber.let {
                            it.decline(1f)
                            filter { matrix ->
                                matrix.frequency(it.value!!.toInt())
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
                saveData()
            }
            .bindLife()

        //撤销
        RxView.clicks(binding.imageRevoke)
            .doOnNext {
                revoke()
                when (viewModel.editMode.value) {
                    EditMode.TimeZero -> {
                        val offset = GPRDataManager.matrixT.timeZero()
                        viewModel.editNumber.value = offset
                    }
                    EditMode.DCFilter -> {
                        viewModel.editNumber.value = 1f
                    }
                    EditMode.Truncation -> {
                        viewModel.editNumber.value = 1f
                    }
                    EditMode.Potential -> {
                        viewModel.editNumber.value = 1f
                    }
                    EditMode.Derivation -> {
                        viewModel.editNumber.value = 1f
                    }
                    EditMode.Background -> {
                        viewModel.editNumber.value = 1f
                    }
                    EditMode.Frequency -> {
                        viewModel.editNumber.value = 10f
                    }
                    else -> {
                        viewModel.editNumber.value = 0f
                    }
                }
            }
            .bindLife()

    }

    fun saveData() {
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

    fun filter(filter: (GPRDataMatrix) -> Single<Unit>) {
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
            if (gprImage.initFlag == 0) {
                gprImage.initFlag += 1
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
    fun onClose(){
        AlertDialog.Builder(context)
            .setTitle("确定放弃编辑吗？")
            .setPositiveButton(
                "确定"
            ) { dialog, which ->
                compositeDisposable.clear()
                GPRDataManager.clear()
                finish()
                activity!!.finish()
                dialog.dismiss()
            }
            .setNegativeButton("取消") { dialog, which ->
                dialog.dismiss()
            }.create().show()
    }

    fun timeZeroCorrect(offset: Int) {
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


    fun updateView(matrix: GPRDataMatrix, operation: (() -> Unit)? = null) {
        gprImage.updateData(matrix)
            .switchThread()
            .netProgressDialog(context!!)
            .doOnSuccess {
                energy_image.updateData(data = matrix)
                operation?.invoke()
            }
            .bindLife()
    }

    val depth = dataInstance.timeWindow * (2.99792458E8 / sqrt(dataInstance.dielectric)) / 2.0E9

    fun measure(verticalLeft: Int, verticalRight: Int, horizontalLeft: Int, horizontalRight: Int) {
        val height =
            abs(horizontalLeft - horizontalRight).toFloat() / dataInstance.samples * depth
        viewModel.measureHeight.value = String.format("高度：%.2f m", height)
        val width = abs(verticalLeft - verticalRight) * dataInstance.distanceInterval
        viewModel.measureWidth.value = String.format("宽度：%.2f m", width)
    }

}
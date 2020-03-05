package com.example.lifecycle.ui.fragment

import android.graphics.Color
import android.util.Log
import android.widget.Toast
import com.example.lifecycle.GPRImageView
import com.example.lifecycle.R
import com.example.lifecycle.databinding.FragmentTopBinding
import com.example.lifecycle.utils.FileUtil
import com.photo.ui.fragment.base.BindingFragment
import com.photo.utils.Constants
import com.photo.utils.netProgressDialog
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_top.*
import timber.log.Timber
import java.io.File
import java.nio.charset.Charset
import java.util.*


class TopFragment : BindingFragment<FragmentTopBinding, TopViewModel>(TopViewModel::class.java, R.layout.fragment_top){

    lateinit var gprImage:GPRImageView
    override fun initView() {
        gprImage = matrix


        val br = Color.red(resources.getColor(R.color.wh))
        val bg = Color.green(resources.getColor(R.color.wh))
        val bb = Color.blue(resources.getColor(R.color.wh))
        val ba = Color.alpha(resources.getColor(R.color.wh))

    }

    override fun initData() {
        val rd3File = File(Constants.RD3)

        Single.just(FileUtil.readFileToMatrix2(rd3File))

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .netProgressDialog(context!!)
            .doOnSuccess {

                Timber.e("xia")
               // it.printMatrix()
//                for(i in 0 until 1500){
//                    Log.d("xia",it.matrix.get(9)?.get(i).toString())
//                }
                gprImage.setGPRData(it)
                gprImage.invalidate()
            }
            .bindLife()

    }
}
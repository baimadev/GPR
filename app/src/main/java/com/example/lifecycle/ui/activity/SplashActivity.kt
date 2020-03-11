package com.example.lifecycle.ui.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.lifecycle.R
import com.example.lifecycle.base.BaseActivity
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.ui.activity.findfileguide.FindFileGuideActivity
import com.example.lifecycle.ui.customview.ColorDialog
import com.example.lifecycle.ui.customview.EditDialog
import com.example.lifecycle.utils.ColorUtils
import com.example.lifecycle.utils.FileUtil
import com.example.lifecycle.utils.SharedPrefModel
import com.photo.utils.Constants
import com.photo.utils.netProgressDialog
import com.photo.utils.switchThread
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import java.io.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (isTaskRoot) init()
        else goToNextActivity()
    }

    private fun init(intent: Intent? = this.intent) {



        bt_import_data.setOnClickListener {
            importFile()
        }

        bt_ensure.setOnClickListener {
            startActivity(
                Intent(this,
                    FindFileGuideActivity::class.java)
            )

        }
    }

    private fun goToNextActivity(intent: Intent? = this.intent) {
        // for the url_scheme , need invoke finish first
        finish()
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(0, 0)
    }


    fun importFile(){
        val rd3File = File(Constants.RD3)
        val radFile = File(Constants.RAD)

        Single.just(FileUtil.readRad(radFile))
            .switchThread()
            .doOnSuccess {
                //判断文件大小
                if(SharedPrefModel.lastTrace>Constants.DefaultTraces){
                    val dialog  = EditDialog(this)
                    dialog.file = rd3File
                    dialog.onPartInput = {

                        Single.just(FileUtil.readFileToMatrix(rd3File, it))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .netProgressDialog(this)
                            .doOnSuccess { matrix ->
                                GPRDataManager.initData(matrix)
                                finish()
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                            .bindLife()
                    }
                    dialog.show()
                }else{
                 //todo 小于1500
                }
            }
            .bindLife()

//        Single.just(FileUtil.readRd3(realRD3))
//            .switchThread()
//            .netProgressDialog(context!!)
//            .bindLife()

    }

}

package com.example.lifecycle.ui.activity


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.lifecycle.R
import com.example.lifecycle.base.BaseActivity
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.ui.activity.findfileguide.FindFileGuideActivity
import com.example.lifecycle.ui.customview.EditDialog
import com.example.lifecycle.ui.customview.EditDialog2
import com.example.lifecycle.utils.ColorUtils
import com.example.lifecycle.utils.FileUtil
import com.example.lifecycle.utils.SharedPrefModel
import com.photo.utils.*
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.dialog_edit_text.*
import java.io.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (isTaskRoot) init()
        else goToNextActivity()
    }

    private fun init(intent: Intent? = this.intent) {

        checkPermission()

        bt_import_data.setOnClickListener {
            importFile()
        }

        bt_ensure.setOnClickListener {
            startActivity(
                Intent(this,
                    FindFileGuideActivity::class.java)
            )

        }

        Single.just(ColorUtils.initColoracion())
            .switchThread()
            .bindLife()

    }

    private fun goToNextActivity(intent: Intent? = this.intent) {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(0, 0)
    }

    //导入文件
    fun importFile(){
        val rd3File = File(Constants.RD3)
        val radFile = File(Constants.RAD)

        Single.just(FileUtil.readRad(radFile))
            .netProgressDialog(this)
            .switchThread()
            .doOnSuccess {
                    //判断文件大小
                    if(SharedPrefModel.lastTrace> SharedPrefModel.defaultTraces){
                            Single.just(FileUtil.readFileToMatrix(rd3File, 1))
                                .netProgressDialog(this)
                                .switchThread()
                                .doOnSuccess { matrix ->
                                    GPRDataManager.initData(matrix)
                                    finish()
                                    startActivity(Intent(this, MainActivity::class.java))
                                }
                                .bindLife()

                    }else{
                        SharedPrefModel.defaultTraces = SharedPrefModel.lastTrace
                        Single.just(FileUtil.readFileToMatrix(rd3File,0 ))
                            .switchThread()
                            .doOnSuccess { matrix ->
                                GPRDataManager.initData(matrix)
                                finish()
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                            .bindLife()
                    }
            }
            .bindLife()
    }


    @SuppressLint("CheckResult")
    fun checkPermission() {
        val permission = RxPermissions(this)
        permission.setLogging(true)

        if (!permission.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) || !permission.isGranted(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            permission.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                .subscribe {
                    if (!it) {
                        Toast.makeText(this, "请在设置中开启权限！", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}

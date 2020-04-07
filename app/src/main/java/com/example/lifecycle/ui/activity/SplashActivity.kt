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
import com.jakewharton.rxbinding2.view.RxView
import com.photo.utils.*
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.dialog_edit_text.*
import java.io.*

class SplashActivity : BaseActivity() {

    val dataInstance = GPRDataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setStatusBarFullTransparent()
        setFitSystemWindow(false)
        if (isTaskRoot) init()
        else goToNextActivity()
    }

    private fun init(intent: Intent? = this.intent) {

        checkPermission()

        //选择文件
        bt_import_data.setOnClickListener {
            startActivity(
                Intent(this,
                    FindFileGuideActivity::class.java)
            )
        }

        //GO!
        RxView.clicks(bt_import)
            .doOnNext {
                importRd3()
            }
            .bindLife()

        //初始化色域
        Single.just(ColorUtils.initColoracion())
            .switchThread()
            .bindLife()

    }

    private fun goToNextActivity(intent: Intent? = this.intent) {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(0, 0)
    }


    //导入文件
    fun importRd3(){
        val rd3File = File(Constants.REAL_RD3)
        val radFile = File(Constants.RAD)

            Single.just(FileUtil.readRad(radFile))
                .netProgressDialog(this)
                .switchThread()
                .doOnSuccess {
                    val dielectric = EditDialog2(this)
                    dielectric.text = "请输入介电常数（默认为6）"
                    dielectric.showInt = 6.0f
                    dielectric.onPartInput = {
                        dataInstance.dielectric = it
                        dielectric.dismiss()
                        //判断文件大小
                        if(dataInstance.lastTrace> SharedPrefModel.defaultTraces){
                            val dialog  = EditDialog(this)
                            dialog.file = rd3File
                            val progressDialog = DialogUtil.showProgressDialogNow(this)
                            dialog.onCloseClick = {
                                progressDialog.dismiss()
                            }
                            dialog.onPartInput = {
                                dialog.dismiss()
                                Single.just(FileUtil.readRd3(rd3File, it))
                                    .switchThread()
                                    .doOnSuccess { matrix ->
                                        progressDialog.dismiss()
                                        GPRDataManager.initData(matrix)
                                        startActivity(Intent(this, MainActivity::class.java))
                                    }
                                    .bindLife()
                            }
                            dialog.show()
                        }else{
                            dataInstance.defaultTraces = dataInstance.lastTrace
                            Single.just(FileUtil.readRd3(rd3File!!,0 ))
                                .switchThread()
                                .doOnSuccess { matrix ->
                                    GPRDataManager.initData(matrix)
                                    finish()
                                    startActivity(Intent(this, MainActivity::class.java))
                                }
                                .bindLife()
                        }
                    }
                    dielectric.show()
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

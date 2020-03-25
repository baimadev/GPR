package com.example.lifecycle.ui.activity.findfileguide

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.lifecycle.R
import com.example.lifecycle.base.BaseActivity
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.ui.activity.MainActivity
import com.example.lifecycle.ui.customview.EditDialog
import com.example.lifecycle.ui.customview.EditDialog2
import com.example.lifecycle.utils.FileUtil
import com.example.lifecycle.utils.SharedPrefModel
import com.jakewharton.rxbinding2.view.RxView
import com.photo.utils.Constants
import com.photo.utils.DialogUtil
import com.photo.utils.netProgressDialog
import com.photo.utils.switchThread
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_find_file.*
import java.io.File
import java.util.*


class FindFileGuideActivity : BaseActivity() {

    val currentDirList = LinkedList<File>()

    lateinit var dirListAdapter :DirListAdapter
    lateinit var fileListAdapter: FileListAdapter
    var radFile : File? = null
    var rd3File : File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_file)
        initRecyclerView()
        RxView.clicks(bt_ensure)
            .doOnNext {
                if(radFile!=null && rd3File!=null){
                    importFile()
                }else{
                    Toast.makeText(this,"请同时选中.rd3和.rad文件！",Toast.LENGTH_SHORT).show()
                }
            }
            .bindLife()
    }

    override fun onBackPressed() {
        returnLast()
    }

    fun returnLast(){
        if(currentDirList.size > 1){
            currentDirList.pollLast()!!.parentFile!!
            val file=currentDirList.last
            val pair = listFileDirName(file)
            dirListAdapter.setNewData(pair.first)
            fileListAdapter.setNewData(pair.second)
        }else{
            super.onBackPressed()
        }
    }

    //导入文件 TODO rd3 rad导入
    fun importFile(){

        Single.just(FileUtil.readRad(radFile!!))
            .netProgressDialog(this)
            .switchThread()
            .doOnSuccess {
                val dielectric = EditDialog2(this)
                dielectric.text = "请输入介电常数（默认为6）"
                dielectric.showInt = 6.0f
                dielectric.onPartInput = {
                    SharedPrefModel.dielectric = it
                    dielectric.dismiss()
                    //判断文件大小
                    if(SharedPrefModel.lastTrace> SharedPrefModel.defaultTraces){
                        val dialog  = EditDialog(this)
                        dialog.file = rd3File
                        val progressDialog = DialogUtil.showProgressDialogNow(this)
                        dialog.onCloseClick = {
                            progressDialog.dismiss()
                        }
                        dialog.onPartInput = {
                            dialog.dismiss()
                            Single.just(FileUtil.readFileToMatrix(rd3File!!, it))
                                .switchThread()
                                .doOnSuccess { matrix ->
                                    progressDialog.dismiss()
                                    GPRDataManager.initData(matrix)
                                    finish()
                                    startActivity(Intent(this, MainActivity::class.java))
                                }
                                .bindLife()
                        }
                        dialog.show()
                    }else{
                        SharedPrefModel.defaultTraces = SharedPrefModel.lastTrace
                        Single.just(FileUtil.readFileToMatrix(rd3File!!,0 ))
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

    fun initRecyclerView(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            val file = Environment.getExternalStorageDirectory()
            currentDirList.addLast(file)
            val pair = listFileDirName(file)
            dirListAdapter = DirListAdapter(pair.first)
            fileListAdapter = FileListAdapter(pair.second)

            val inflater = LayoutInflater.from(this)
            fileListAdapter.setEmptyView(inflater.inflate(R.layout.item_empty,null,false))
            //左侧空则返回上一级
            val viewReturn = inflater.inflate(R.layout.item_return,null,false)
            viewReturn.setOnClickListener {
                returnLast()
            }
            dirListAdapter.setEmptyView(viewReturn)

            dirListAdapter.setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as File
                if(item.isDirectory){
                    val pair = listFileDirName(item)
                    currentDirList.addLast(item)
                    adapter.setNewData(pair.first)
                    fileListAdapter.setNewData(pair.second)
                }
            }

            fileListAdapter.setOnItemClickListener { adapter, view, position ->

                val file = adapter.data[position] as File
                val check = view.findViewById<ImageView>(R.id.image_select)

                //todo rd3解码
                val suffix: String = file.name.substring(file.name.lastIndexOf(".") + 1)
                when (suffix) {
                    "rad" -> {
                        if(radFile == file ){
                            check.visibility = View.INVISIBLE
                            radFile = null
                        }else{
                            check.visibility = View.VISIBLE
                            radFile = file
                        }
                    }
                    "asc" -> {
                        if(rd3File == file ){
                            check.visibility = View.INVISIBLE
                            rd3File = null
                        }else{
                            check.visibility = View.VISIBLE
                            rd3File = file
                        }
                    }
                    else -> {
                        Toast.makeText(this,"请选择.rd3或.rad文件！",Toast.LENGTH_SHORT).show()
                    }
                }

            }

            rv_files_list.adapter = fileListAdapter
            rv_files_list.layoutManager = StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL)
            rv_dirs_list.adapter = dirListAdapter
            rv_dirs_list.layoutManager = LinearLayoutManager(this)
        }
    }

    fun listFileDirName(file: File) : Pair<MutableList<File>,MutableList<File>>{
        val dirs = mutableListOf<File>()
        val files = mutableListOf<File>()
        if(file.isDirectory){
            if(file.listFiles() != null){

                file.listFiles()!!.forEach {
                    if(it.isDirectory) {
                        dirs.add(it)
                    }else{
                        files.add(it)       
                    }
                }
            }
        }
        dirs.sortBy {
            it.name
        }

        files.sortBy {
            it.name
        }
        return dirs to files
    }

}
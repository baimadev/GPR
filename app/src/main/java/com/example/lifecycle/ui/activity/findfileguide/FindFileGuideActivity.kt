package com.example.lifecycle.ui.activity.findfileguide

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifecycle.R
import com.example.lifecycle.base.BaseActivity
import kotlinx.android.synthetic.main.activity_find_file.*
import java.io.File
import java.util.*

class FindFileGuideActivity : BaseActivity() {

    val currentFileList = LinkedList<File>()

    lateinit var fileListAdapter :FileListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_file)
        initRecyclerView()
    }

    override fun onBackPressed() {
        if(currentFileList.size > 1){
            val file=currentFileList.pollLast()!!.parentFile!!
            val files = listFileDirName(file)
            fileListAdapter.setNewData(files)
        }else{
            super.onBackPressed()
        }
    }

    fun initRecyclerView(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            val file = Environment.getExternalStorageDirectory()
            currentFileList.addLast(file)
            val files = listFileDirName(file)
            fileListAdapter = FileListAdapter(files)
            fileListAdapter.setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as File
                if(item.isDirectory){
                    val files = listFileDirName(item)
                    if(files.isNotEmpty()) currentFileList.addLast(files[0].parentFile)
                    adapter.setNewData(files)
                }else{
                    //选中 提取文件 跳转
                    Log.d("xia","$item.name")

                }
            }

            rv_file_list.adapter = fileListAdapter
            rv_file_list.layoutManager = LinearLayoutManager(this)
        }
    }

    fun listFileDirName(file: File) : MutableList<File>{
        val files = mutableListOf<File>()
        if(file.isDirectory){
            if(file.listFiles() != null){
                files.addAll(file.listFiles()!!)
            }
        }
        return files
    }

}
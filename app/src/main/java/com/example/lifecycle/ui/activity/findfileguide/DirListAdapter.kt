package com.example.lifecycle.ui.activity.findfileguide

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.lifecycle.R
import kotlinx.android.synthetic.main.item_rv.view.*
import java.io.File

class DirListAdapter(files: MutableList<File>) :
    BaseQuickAdapter<File, BaseViewHolder>(R.layout.item_text, files) {
    override fun convert(helper: BaseViewHolder, item: File) {
        helper.setText(R.id.text,item.name)
        val drawable1 = context.getDrawable(R.drawable.ic_folder_black_24dp)
        val drawable2 = context.getDrawable(R.drawable.ic_insert_drive_file_black_24dp)
        if(item.isDirectory){
            helper.setImageDrawable(R.id.item_image,drawable1)
        }else{
            helper.setImageDrawable(R.id.item_image,drawable2)
        }

    }



}
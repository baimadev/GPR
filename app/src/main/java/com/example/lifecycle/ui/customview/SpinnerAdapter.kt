package com.example.lifecycle.ui.customview



import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import com.example.lifecycle.R

class SpinnerAdapter (context: Context,val list:List<Int>,val resourceId: Int): ArrayAdapter<Int>(context,resourceId) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view :View
        if(convertView != null){
            view = convertView
        }else{
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(resourceId,parent,false)
        }
        view.findViewById<ImageView>(R.id.sp_image).setImageResource(list[position])

        return view

    }

}
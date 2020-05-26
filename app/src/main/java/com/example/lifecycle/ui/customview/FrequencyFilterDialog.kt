package com.example.lifecycle.ui.customview

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lifecycle.R
import com.example.lifecycle.databinding.DialogFrFilterBinding
import com.example.lifecycle.model.GPRDataManager
import com.example.lifecycle.utils.WindowType
import kotlinx.android.synthetic.main.dialog_fr_filter.*
import kotlinx.android.synthetic.main.dialog_signal_amp.*
import kotlinx.android.synthetic.main.dialog_signal_amp.image_done


class FrequencyFilterDialog (mContext: Context): Dialog(mContext, R.style.MyDialog){

    var onEnsure: ((Int,WindowType,Float,Float) -> Unit)? = null
    var lBand = -1f
    var hBand = -1f
    var lxType = 1
    var clxType = WindowType.juxing
    var aContext:Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(context)
        val binding = DataBindingUtil.inflate<DialogFrFilterBinding>(inflater,R.layout.dialog_fr_filter,null,false)
        setContentView(binding.root)
        initWindow()

        val lx = arrayListOf("低通","高通","带通","带阻")
        val clx = arrayListOf("矩形窗口","图基窗口","三角窗口","汉宁窗口","海明窗口","布拉克曼窗口")
        sp_lx.attachDataSource(lx)
        sp_clx.attachDataSource(clx)
        sp_lx.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            lxType = position+1
        }
        sp_clx.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            clxType = when(position){
                0 -> WindowType.juxing
                1 -> WindowType.tuji
                2 -> WindowType.sanjiao
                3 -> WindowType.hanning
                4 -> WindowType.haimin
                5 -> WindowType.bula
                else -> WindowType.empty
            }
        }

        binding.btClose.setOnClickListener { this.dismiss() }


        edit_hband.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if(it.toString() != ""){
                        hBand = s.toString().toFloat()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        edit_lband.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if(it.toString() != ""){
                        lBand = s.toString().toFloat()
                    }

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })


        image_done.setOnClickListener {
            checkParam()
        }
    }

    @SuppressLint("ShowToast")
    private fun checkParam(){

        if (lxType == 1 && lBand == -1f){
            Toast.makeText(aContext,"请输入下边界！",Toast.LENGTH_SHORT).show()
            return
        }
        if (lxType == 2 && hBand == -1f){
            Toast.makeText(aContext,"请输入上边界！",Toast.LENGTH_SHORT).show()
            return
        }
        if (lxType >= 3 && ((lBand == -1f)||(hBand == -1f))){
            Toast.makeText(aContext,"请输入上下边界！",Toast.LENGTH_SHORT).show()
            return
        }
        onEnsure?.invoke(lxType,clxType,lBand,hBand)
        this.dismiss()
    }

    private fun initWindow(){
        window!!.setGravity(Gravity.CENTER_VERTICAL)
        val lp = window!!.attributes
        lp.width = CoordinatorLayout.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        lp.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        //点击外部不消失
        setCanceledOnTouchOutside(false)
    }

}
package com.photo.utils

import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.lifecycle.R
import java.io.File

@BindingAdapter("imgUrl")
fun setImgFromUrl(imageView: ImageView, url: String?) {
    if (TextUtils.isEmpty(url)) return
    Glide.with(imageView.context)
        .load(url)
        .into(imageView)
}

@BindingAdapter("imgPath")
fun setImgFromLocalPath(imageView: ImageView, url: String?) {
    if (TextUtils.isEmpty(url)) imageView.setImageResource(R.drawable.icon_misshon_photo)
    else
        Glide.with(imageView.context)
            .load(Uri.fromFile(File(url)))
            .into(imageView)
}

@BindingAdapter("isSelected")
fun setViewSelected(view: View , selected : Boolean){
    view.isSelected = selected
}

@BindingAdapter("android:src")
fun setSrc(view: ImageView, bitmap: Int) {
    view.setImageResource(bitmap)
}

@BindingAdapter(value = ["imageUrl", "thumbnailUrl"], requireAll = true)
fun loadImageUrl(view: ImageView, url: String?, thumbnail: String?) {
    val path = if(TextUtils.isEmpty(url)) thumbnail else url
    Glide.with(view)
            .asBitmap()
            .centerCrop()
            .placeholder(R.drawable.icon_select_placeholder)
            .error(R.drawable.icon_select_placeholder)
            .load(path)
            .into(view)
}

@BindingAdapter(value = ["templateUrl", "photoUrl"], requireAll = true)
fun loadTemplate(view: ImageView, templateUrl: String?, photoUrl: String?) {
    val path = if(TextUtils.isEmpty(templateUrl)) photoUrl else templateUrl
    Glide.with(view)
            .asBitmap()
            .centerCrop()
            .load(path)
            .into(view)
}

@BindingAdapter(value = ["visible"])
fun setVisible(view: View, visible: Boolean?) {
    if (visible == null || !visible) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}

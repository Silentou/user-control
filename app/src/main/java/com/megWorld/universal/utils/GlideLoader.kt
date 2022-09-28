package com.megWorld.universal.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.megWorld.universal.R
import java.io.IOException

class GlideLoader(val context: Context) {
    fun loadUserPicture(image: Any,imageView: ImageView){
        try{
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.userpic)
                .into(imageView)
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}
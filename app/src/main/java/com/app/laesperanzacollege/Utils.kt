package com.app.laesperanzacollege

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.ByteArrayOutputStream

class Utils {
    companion object
    {
        fun setImage(mImage:ByteArray,mImageView: ImageView?)
        {
            mImageView?.setImageBitmap(mImage.toBitmap())
        }

        fun Bitmap.toByteArray():ByteArray{
            ByteArrayOutputStream().apply {
                compress(Bitmap.CompressFormat.JPEG,10,this)
                return toByteArray()
            }
        }

        fun ByteArray.toBitmap(): Bitmap {
            return BitmapFactory.decodeByteArray(this,0,size)
        }
    }
}
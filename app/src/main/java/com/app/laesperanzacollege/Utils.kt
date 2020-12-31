package com.app.laesperanzacollege

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import kotlin.math.pow

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

        fun floorDiv(x: Int, y: Int): Int {
            var r = x / y
            // if the signs are different and modulo not zero, round down
            if ((x.toDouble().pow(y)) < 0 && (r * y != x)) {
                r--
            }
            return r
        }
        fun mostrarContiuar(visibility:Int, mView:View?)
        {
            mView?.visibility=visibility
        }

        fun letrasEditTextEstaLlena(viewList:ArrayList<EditText>): Boolean {

            if(viewList.size>0)
            {
                viewList.forEach { x->if(x.text.toString()==""){return false} }
            }
            return true
        }
    }
}
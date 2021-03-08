package Observers

import android.graphics.Bitmap

interface EstudianteObserver {
fun updateImage(mByteArray: ByteArray)
    fun updateUser(user:String)
    fun updatePass(pass:String)
}
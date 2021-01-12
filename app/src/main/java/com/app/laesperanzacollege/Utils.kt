package com.app.laesperanzacollege

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_agregar_estu.*
import java.io.ByteArrayOutputStream
import kotlin.math.pow
import kotlin.random.Random

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

        private fun ByteArray.toBitmap(): Bitmap {
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

        fun generarAlfabeto(preAlfabeto: String):String {
            val randomValues = List(8) { Random.nextInt(65, 90).toChar() }
            return desordenar("$preAlfabeto${randomValues.joinToString(separator = "")}")
        }

        private fun desordenar(theWord: String):String {

            val theTempWord=theWord.toMutableList()

            for (item in 0..Random.nextInt(theTempWord.count()/2,theTempWord.count()-1))
            {
                val indexA=Random.nextInt(theTempWord.count()-1)
                val indexB=Random.nextInt(theTempWord.count()-1)

                val temp=theTempWord[indexA]

                theTempWord[indexA]=theTempWord[indexB]
                theTempWord[indexB]=temp
            }

            return theTempWord.joinToString(separator = "")
        }
        fun spanCalc(mRecy:RecyclerView,context: Context):Int
        {
            val viewWidth: Int = mRecy.width
            val cardViewWidth: Float =context.resources.getDimension(R.dimen.card_quizzes)
            return floorDiv(viewWidth,cardViewWidth.toInt())
        }

        fun crearCustomSnackbar(stick:View,backgroundColor:Int,resIcon:Int,message:String,layoutInflater:LayoutInflater):Snackbar
        {
            val mSnack= Snackbar.make(stick,"", Snackbar.LENGTH_SHORT)
            mSnack.view.setBackgroundColor(backgroundColor)

            val mSnackLayout=mSnack.view as Snackbar.SnackbarLayout
            val mcustomView=layoutInflater.inflate(R.layout.snackbar_custom,null)

            mcustomView.findViewById<ImageView>(R.id.snackbar_icon).setImageResource(resIcon)
            mcustomView.findViewById<TextView>(R.id.snackbar_message).text=message

            mSnackLayout.addView(mcustomView)
            return mSnack
        }

    }
}
package com.app.laesperanzacollege

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import javax.xml.validation.Validator

class Validador {
    companion object
    {
        fun validarCantidad(myMessage: LinearLayoutCompat, myListElement: List<Any>)
        {
            if(myListElement.isEmpty())
            {
                myMessage.visibility= View.VISIBLE
            }
            else
            {
                myMessage.visibility= View.GONE
            }
        }
        fun validarCantidad(myMessage: TextView, myListElement: List<Any>)
        {
            if(myListElement.isEmpty())
            {
                myMessage.visibility= View.VISIBLE
            }
            else
            {
                myMessage.visibility= View.GONE
            }
        }
    }
}
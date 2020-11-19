package com.app.laesperanzacollege

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.app.laesperanzadao.RespuestaDAO
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Respuesta
import kotlinx.android.synthetic.main.mainquizfragment.view.*

class MainQuizFragment(private var myPpregunta:Pregunta,private var actual:Int,private var final:Int): Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myFrag=inflater.inflate(R.layout.mainquizfragment,container,false)

        myFrag.pregunta.text=myPpregunta.descripcion
        myFrag.preguntaActual.text=actual.toString()
        myFrag.preguntaFinal.text=final.toString()

        opcionRespuesta(myPpregunta.opcionDeRespuestaId,respuestas(myPpregunta.id),myFrag.viewRespuestas)

        return myFrag
    }

    fun respuestas(preguntaId:Int?):ArrayList<Respuesta>
    {
        return RespuestaDAO(activity!!.applicationContext).ListarRespuestas(preguntaId!!)
    }

    fun opcionRespuesta(opcionId:Int?,respuestas:ArrayList<Respuesta>,viewResp:LinearLayout)
    {
        when(opcionId)
        {
            1->
            {
                for (item in respuestas)
                {
                    val myCheckBox = CheckBox(activity!!.applicationContext)
                    myCheckBox.text=item.descripcion
                    myCheckBox.id=item.id!!

                    myCheckBox.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    viewResp.addView(myCheckBox)
                }
            }
            2->
            {
                for (item in respuestas)
                {
                    val myButton=Button(activity!!.applicationContext)
                    myButton.text=item.descripcion
                    myButton.id=item.id!!

                    myButton.layoutParams=LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                             ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    viewResp.addView(myButton)
                }
            }
            3->
            {

            }
        }
    }
}
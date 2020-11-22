package com.app.laesperanzacollege

import Observers.RespuestaObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import com.app.laesperanzadao.RespuestaDAO
import com.app.laesperanzaedm.model.Respuesta
import kotlinx.android.synthetic.main.activity_agregar_respuesta.*
import kotlinx.android.synthetic.main.activity_agregar_respuesta.view.*

class AgregarRespuestaActivity : AppCompatActivity() {
    var correcta=false
    var preguntaId:Int?=null
    var myRespuestaDAO:RespuestaDAO?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_respuesta)

        myRespuestaDAO=RespuestaDAO(this)

        var myData=intent.extras?.get("PREGUNTAID")

        if(myData!=null)
        {
            preguntaId=Integer.parseInt(myData.toString())
        }

        RgbCorrecta.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->

            when
         {
             RgbCorrecta.rbtSi.isChecked->{
                 correcta=true
             }
             RgbCorrecta.rbtNo.isChecked->{
                 correcta=false
             }
         }
        })
        btnGuardarRes.setOnClickListener {
            if(validar())
            {
                var myRestoSave=Asignar()
                if(myRespuestaDAO!!.Insertar(myRestoSave))
                {
                    myRestoSave= myRespuestaDAO?.BuscarRespuesta(myRestoSave.descripcion.toString())!!
                    Toast.makeText(this,"Guardado Exitosamente",Toast.LENGTH_LONG).show()
                    myRespuestaObserver?.respuestaSaved(myRestoSave)
                    this.finish()
                }
                else
                    Toast.makeText(this, "Ocurrio un error",Toast.LENGTH_LONG).show()
            }

        }
    }

    fun Asignar():Respuesta {
        var myRes = Respuesta()
        myRes.descripcion = edtDesRes.text.toString()
        myRes.correcta=correcta
        myRes.preguntaId=preguntaId

        return myRes
    }

    fun validar():  Boolean
    {
        if(edtDesRes.text!!.isEmpty())
        {
            txtDesRes.error="Respuesta Vacia"
            return false
        }
        else
            txtDesRes.error=null

        return true
    }

    companion object
    {
        var myRespuestaObserver:RespuestaObserver?=null
    }
}
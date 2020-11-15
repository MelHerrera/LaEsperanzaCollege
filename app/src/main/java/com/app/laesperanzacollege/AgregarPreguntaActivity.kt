package com.app.laesperanzacollege

import Observers.PreguntaObserver
import Observers.RespuestaObserver
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.laesperanzacollege.adaptadores.PreguntaRespuestaAdapter
import com.app.laesperanzadao.OpcionesDeRespuestaDAO
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzadao.RespuestaDAO
import com.app.laesperanzaedm.model.OpcionDeRespuesta
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Respuesta
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_agregar_pregunta.*
import kotlin.math.abs


class AgregarPreguntaActivity : AppCompatActivity(), RespuestaObserver {
    var myPreguntaDAO:PreguntaDAO?=null
    var myRespuestaDAO:RespuestaDAO?=null
    var myOpcionDeRespuestaDAO:OpcionesDeRespuestaDAO?=null
    var quizzId:Int?=null
    var quizzNombre:String=""
    var myListOpciones:ArrayList<OpcionDeRespuesta> = arrayListOf()
    var myListPregunta:ArrayList<Pregunta>?=null
    var myListRespuesta:ArrayList<Respuesta>?=null
    var myPreguntaRespuestaAdapter:PreguntaRespuestaAdapter?=null
    var opcionDeRespuestaId:Int?=null
    var cantidadPreguntas:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_pregunta)

        edtPregunta.requestFocus()
        val myToolbar=findViewById<Toolbar>(R.id.toolbar)
        myToolbar.title=""
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



        app_barActPregunta.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                toolbar_layoutActPregunta.isTitleEnabled = true
                toolbar_layoutActPregunta.title = getString(R.string.preguntas)
            } else {
                toolbar_layoutActPregunta.isTitleEnabled = false
            }

        })

        cantidadPreguntas=txtCantidadPreguntas
        myPreguntaDAO= PreguntaDAO(this)
        myRespuestaDAO=RespuestaDAO(this)

        var myData=intent.extras?.get("QUIZZID")

        if(myData!=null)
        {
            quizzId=Integer.parseInt(myData.toString())
        }

        myData=intent.extras?.get("QUIZZNOM")

        if(myData!=null)
        {
            pregQuiz.visibility=View.VISIBLE
            pregQuiz.text="Unidad ${myData.toString()}"
        }
        else
        {
            pregQuiz.text=""
            pregQuiz.visibility=View.GONE
        }


        myOpcionDeRespuestaDAO= OpcionesDeRespuestaDAO(this)
        myListOpciones= myOpcionDeRespuestaDAO!!.listarOpciones()

        if(myListOpciones.size>0)
        {
            for (item in myListOpciones)
            {

                val myChipChoice= Chip(this)
                //var myRadioButton:RadioButton= RadioButton(this)
                myChipChoice.gravity = (Gravity.CENTER_VERTICAL or Gravity.START)
                //myRadioButton.text =item.descripcion
                myChipChoice.text=item.descripcion
                myChipChoice.isCheckable=true

                myChipChoice.setTextColor(Color.WHITE)
                //myRadioButton.setTextColor(Color.WHITE)

                myChipChoice.setChipBackgroundColorResource(R.color.colorAccent)

                // rgbOpciones.addView(myRadioButton)
                rgbOpciones.addView(myChipChoice)
            }
        }

        myListPregunta=myPreguntaDAO?.ListarPreguntas(quizzId!!)
        if(cantidadPreguntas!=null && myListPregunta!=null) Validador.validarCantidad(cantidadPreguntas!!, myListPregunta!!)


        myListRespuesta=myRespuestaDAO?.ListarRespuestas()
        myPreguntaRespuestaAdapter= PreguntaRespuestaAdapter(myListPregunta!!, myListRespuesta!!)
        pregs.layoutManager=LinearLayoutManager(this)
        pregs.adapter=myPreguntaRespuestaAdapter

        rgbOpciones.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId!=-1)
            {
                val index=rgbOpciones.indexOfChild(findViewById(checkedId))
                opcionDeRespuestaId=myListOpciones[index].id
                Toast.makeText(
                    this,
                    "Onchecked in ${myListOpciones[index].descripcion}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else
                opcionDeRespuestaId=null

        }

        btnGuar.setOnClickListener {

            if(validar())
            {
                var myPreguntaToSave=Asignar()

                if(myPreguntaDAO?.Insertar(myPreguntaToSave)!!)
                {
                    myPreguntaToSave= myPreguntaDAO?.BuscarPregunta(myPreguntaToSave.descripcion.toString())!!

                    myListPregunta?.add(myPreguntaToSave)
                    myPreguntaRespuestaAdapter?.notifyDataSetChanged()
                    if(cantidadPreguntas!=null && myListPregunta!=null) Validador.validarCantidad(
                        cantidadPreguntas!!,
                        myListPregunta!!
                    )

                    myPreguntaObserver?.preguntaSaved(myPreguntaToSave)
                    AgregarRespuestaActivity.myRespuestaObserver=this

                    Toast.makeText(this, "Se Agrego con Exito", Toast.LENGTH_LONG).show()
                    edtPregunta.text?.clear()

                    rgbOpciones.clearCheck()
                    opcionDeRespuestaId=null
                }
            }
        }
    }

    fun Asignar():Pregunta
    {
        val myPregunta=Pregunta()
        myPregunta.quizzId=quizzId
        myPregunta.descripcion=edtPregunta.text.toString()
        myPregunta.opcionDeRespuestaId=opcionDeRespuestaId
        return myPregunta
    }

    fun validar():Boolean
    {
        if(edtPregunta.text!!.isEmpty())
        {
            txtPregunta.error="Pregunta Vacia"
            return false
        }
        else
            txtPregunta.error=null

        if(opcionDeRespuestaId==null)
        {
            Toast.makeText(this, "Opcion de Respuesta no Seleccionada", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
    companion object
    {
        var myPreguntaObserver:PreguntaObserver?=null
    }

    override fun respuestaSaved(respuesta: Respuesta) {
        myListRespuesta=myRespuestaDAO?.ListarRespuestas()
        myPreguntaRespuestaAdapter= PreguntaRespuestaAdapter(myListPregunta!!, myListRespuesta!!)
        pregs.layoutManager=LinearLayoutManager(this)
        pregs.adapter=myPreguntaRespuestaAdapter
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
package com.app.laesperanzacollege

import Observers.PreguntaObserver
import Observers.RespuestaObserver
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.laesperanzacollege.adaptadores.PreguntaRespuestaAdapter
import com.app.laesperanzadao.OpcionesDeRespuestaDAO
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzadao.RespuestaDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.OpcionDeRespuesta
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Quiz
import com.app.laesperanzaedm.model.Respuesta
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_agregar_pregunta.*
import kotlin.math.abs


class AgregarPreguntaActivity : AppCompatActivity(),RespuestaObserver {
    var myPreguntaDAO:PreguntaDAO?=null
    var myRespuestaDAO:RespuestaDAO?=null
    var myOpcionDeRespuestaDAO:OpcionesDeRespuestaDAO?=null
    var quizz:Quiz= Quiz()
    var quizzNombre:String=""
    var myListOpciones:ArrayList<OpcionDeRespuesta> = arrayListOf()
    var myListPregunta:ArrayList<Pregunta>?=null
    var myListRespuesta:ArrayList<Respuesta>?=null
    var myPreguntaRespuestaAdapter:PreguntaRespuestaAdapter?=null
    var opcionDeRespuestaId:Int?=null
    var cantidadPreguntas:LinearLayoutCompat?=null
    var mOperacion:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_pregunta)


        mOperacion=intent.extras?.get("OPERACION").toString()
        val myData=intent.extras?.get("QUIZZ")
        //edtPregunta.requestFocus()

        val myToolbar=findViewById<Toolbar>(R.id.toolbar)
        myToolbar.title=getString(R.string.txt_agregarPregunta)
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if(mOperacion==OperacionesCrud.Editar.toString())
        {
            myToolbar.navigationIcon=ContextCompat.getDrawable(this,R.drawable.ic_clear)
            btnGuar.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_save))
        }

        //asignar dinamicamente el texto que tendra el textview cuando no hayan datos
        txtCantidadPreguntas.text=getString(R.string.sin_datos,getString(R.string.preguntas))

        app_barActPregunta.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val nestedScroolParams = nestedPreg.layoutParams as CoordinatorLayout.LayoutParams
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                toolbar_layoutActPregunta.isTitleEnabled = true
                toolbar_layoutActPregunta.title = getString(R.string.txt_agregarPregunta)

                val params = toolbar.layoutParams
                val newParams: CollapsingToolbarLayout.LayoutParams
                newParams = if (params is CollapsingToolbarLayout.LayoutParams) {
                    params
                } else {
                    CollapsingToolbarLayout.LayoutParams(params)
                }
                newParams.collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
                myToolbar.layoutParams = newParams
                myToolbar.requestLayout()

                nestedScroolParams.setMargins(3, 3, 3, 0)
            } else {
                toolbar_layoutActPregunta.isTitleEnabled = false
                nestedScroolParams.setMargins(3, 70, 3, 0)
            }
            nestedPreg.layoutParams = nestedScroolParams
        })

        cantidadPreguntas=viewCantidadPreguntas
        myPreguntaDAO= PreguntaDAO(this)
        myRespuestaDAO=RespuestaDAO(this)


        if(myData!=null)
        {
            quizz=myData as Quiz
        }

        pregQuiz.visibility=View.VISIBLE
        pregQuiz.text="Quizz: ${quizz.nombre}"


        myOpcionDeRespuestaDAO= OpcionesDeRespuestaDAO(this)
        myListOpciones= myOpcionDeRespuestaDAO!!.listarOpciones()

        if(myListOpciones.size>0)
        {
            for (item in myListOpciones)
            {
                val myChipChoice= Chip(this)
                myChipChoice.gravity = (Gravity.CENTER_VERTICAL or Gravity.START)
                myChipChoice.text=item.descripcion
                myChipChoice.isCheckable=true

                myChipChoice.setTextColor(Color.WHITE)
                myChipChoice.setChipBackgroundColorResource(R.color.colorAccent)

                rgbOpciones.addView(myChipChoice)
            }
        }

        myListPregunta=myPreguntaDAO?.ListarPreguntas(quizz.quizId!!)
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
            }
            else
                opcionDeRespuestaId=null

        }

        btnGuar.setOnClickListener {

            if(validar())
            {
                val myPreguntaToSave=Asignar()
                val mIntent= Intent(this,RespuestaActivity::class.java)
                mIntent.putExtra("PREGUNTA",myPreguntaToSave)
                mIntent.putExtra("OPERACION", OperacionesCrud.Agregar)
                RespuestaActivity.myRespuestaObserver=this
                startActivity(mIntent)

                //limpiar controles y variables
                edtPregunta.text?.clear()
                rgbOpciones.clearCheck()
                opcionDeRespuestaId=null
            }
        }
    }

    fun Asignar():Pregunta
    {
        val myPregunta=Pregunta()
        myPregunta.quizzId=quizz.quizId
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
            val mSnack= Utils.crearCustomSnackbar(
                viewPrinPreg, Color.RED, android.R.drawable.stat_notify_error,
                getString(R.string.error_incomplete_data,getString(R.string.txt_opcionRespuesta)), layoutInflater
            )
            mSnack.show()
            return false
        }

        return true
    }
    companion object
    {
        var myPreguntaObserver:PreguntaObserver?=null
    }

    override fun respuestaSaved(newRespuestas: ArrayList<Respuesta>) {
        //do nothing
    }

    override fun preguntaSaved(pregunta: Pregunta, mListRespuestas:ArrayList<Respuesta>) {
        myListPregunta?.add(pregunta)
        myListRespuesta?.addAll(mListRespuestas)
        myPreguntaRespuestaAdapter?.notifyDataSetChanged()

        if(cantidadPreguntas!=null && myListPregunta!=null) Validador.validarCantidad(cantidadPreguntas!!, myListPregunta!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        if(mOperacion==OperacionesCrud.Editar.toString())
        {
            this.finish()
            startActivity(Intent(this,QuizzesActivity::class.java))
        }
        else
        onBackPressed()
        return true
    }
}
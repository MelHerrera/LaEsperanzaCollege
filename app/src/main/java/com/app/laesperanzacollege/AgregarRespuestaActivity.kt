package com.app.laesperanzacollege

import Observers.PreguntaObserver
import Observers.RespuestaObserver
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.RespuestaAdapter
import com.app.laesperanzacollege.interfaces.NotifyUpdateRecyclerView
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzadao.RespuestaDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Respuesta
import kotlinx.android.synthetic.main.activity_agregar_pregunta.*
import kotlinx.android.synthetic.main.activity_agregar_quiz.*
import kotlinx.android.synthetic.main.activity_agregar_respuesta.*
import kotlinx.android.synthetic.main.activity_agregar_respuesta.view.*

class AgregarRespuestaActivity : AppCompatActivity(),NotifyUpdateRecyclerView {
    private var correcta=true
    private var pregunta:Pregunta= Pregunta()
    private var myRespuestaDAO:RespuestaDAO?=null
    private var mListRespuestas:ArrayList<Respuesta> = arrayListOf()
    private var mLayoutManager:RecyclerView.LayoutManager?=null
    private var mRespuestaAdapter:RespuestaAdapter?=null
    private var linearCantidadResp:LinearLayoutCompat?=null
    private var operacion:OperacionesCrud?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_respuesta)

        linearCantidadResp=viewCantidadRespuestas

        //asignar dinamicamente el texto que tendra el textview cuando no hayan datos
        txtCantidadRespuestas.text=getString(R.string.sin_datos,getString(R.string.respuesta))

        if(linearCantidadResp!=null) Validador.validarCantidad(linearCantidadResp!!,mListRespuestas)

        pregunta=intent.extras?.get("PREGUNTA") as Pregunta
        operacion=intent.extras?.get("OPERACION") as OperacionesCrud

        myRespuestaDAO=RespuestaDAO(this)
        mLayoutManager=LinearLayoutManager(this)
        mRespuestaAdapter= RespuestaAdapter(mListRespuestas,this)
        val mItemDecoration= DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        recyclerViewResp.addItemDecoration(mItemDecoration)
        recyclerViewResp.layoutManager=mLayoutManager
        recyclerViewResp.adapter=mRespuestaAdapter

        recyclerViewResp.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
               if(newState==RecyclerView.SCROLL_STATE_IDLE)
                   btnGuardarRes.visibility=View.VISIBLE
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
                    btnGuardarRes.visibility=View.GONE

                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        RgbCorrecta.setOnCheckedChangeListener { _, _ ->
            when {
                RgbCorrecta.rbtSi.isChecked->{
                    correcta=true
                }
                RgbCorrecta.rbtNo.isChecked->{
                    correcta=false
                }
            }
        }

        btnAgregarRes.setOnClickListener {
            if(validar())
            {
                //guardar temporalmente la lista
                val myRestoSave=asignar()
                mListRespuestas.add(myRestoSave)
                updateRecycler()
                if(linearCantidadResp!=null) Validador.validarCantidad(linearCantidadResp!!,mListRespuestas)
                edtDesRes.text?.clear()
                puedeGuardar=true
            }
        }

        btnGuardarRes.setOnClickListener {
            if(puedeGuardar)
            {
                if(operacion==OperacionesCrud.Agregar) {
                    if(PreguntaDAO(this).Insertar(pregunta))
                    {
                        val myPreguntaToSave= PreguntaDAO(this).BuscarPregunta(pregunta.descripcion.toString())!!

                        if(myPreguntaToSave.id!=-1)
                            guardarRespuestas(myPreguntaToSave)

                        myRespuestaObserver?.preguntaSaved(myPreguntaToSave,mListRespuestas)
                        mPreguntaObserver?.preguntaSaved(myPreguntaToSave)
                    }
                }
                else
                {
                    if(pregunta.id!=-1)
                        guardarRespuestas(pregunta)
                    myRespuestaObserver?.respuestaSaved(mListRespuestas)
                }
                this.finish()
            }
            else
            {
                val mSnack= Utils.crearCustomSnackbar(
                    viewAgregarResp, Color.RED, android.R.drawable.stat_notify_error,
                    getString(R.string.sin_datos,getString(R.string.respuesta)), layoutInflater
                )
                mSnack.show()
            }
        }
    }

    private fun guardarRespuestas(mPreguntaToSave:Pregunta) {
        mListRespuestas.forEach {
            it.preguntaId=mPreguntaToSave.id

            if(myRespuestaDAO!!.Insertar(it))
            {
                val mResSaved= myRespuestaDAO?.BuscarRespuesta(it.descripcion.toString())!!
                it.id=mResSaved.id
            }
            else
                Toast.makeText(this, "Ocurrio un error grandote",Toast.LENGTH_LONG).show()
        }
    }

    private fun updateRecycler() {
        mRespuestaAdapter?.notifyDataSetChanged()
    }

    private fun asignar():Respuesta {
        val myRes = Respuesta()
        myRes.descripcion = edtDesRes.text.toString()
        myRes.correcta=correcta
        myRes.preguntaId=pregunta.id

        return myRes
    }

    private fun validar():  Boolean
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
        var puedeGuardar=false
        var mPreguntaObserver:PreguntaObserver?=null
    }

    override fun updateRecy() {
        updateRecycler()
    }
}
package com.app.laesperanzacollege

import Observers.FiltroObserver
import Observers.PreguntaObserver
import Observers.QuizzObserver
import Observers.UnidadObserver
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.QuizPreguntaAdapter
import com.app.laesperanzacollege.adaptadores.UnidAdapter1
import com.app.laesperanzadao.GradoDAO
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzadao.UnidadDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.database.UnidadContract
import com.app.laesperanzaedm.model.Grado
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Quiz
import com.app.laesperanzaedm.model.Unidad
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.activity_agregar_quiz.*
import kotlinx.android.synthetic.main.item_estu.*
import java.util.function.Consumer
import kotlin.math.abs


class AgregarQuizActivity : AppCompatActivity(),UnidadObserver,PreguntaObserver,FiltroObserver {
    var myListUnidad: ArrayList<Unidad>? = null
    var myListQuizz: ArrayList<Quiz>? = null
    var myUnidadDAO: UnidadDAO? = null
    var myPreguntaDAO: PreguntaDAO? = null
    var myQuizDAO: QuizDAO? = null
    var myUnidadesAdapter: UnidAdapter1? = null
    var myQuizPreguntaAdapter: QuizPreguntaAdapter? = null
    var myListPregunta: ArrayList<Pregunta>? = null
    var myQuiz: Quiz? = null
    var numUnidad: Int? = null
    var max: Int? = null
    var CantidadDeUnidades:LinearLayout?=null
    var CantidadQuizz:TextView?=null
    var quizzEstado:Int=0 //Por defecto sin inciar
    var gradoDAO:GradoDAO?=null
    var listaGrados:ArrayList<Grado>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_quiz)
        val myToolbar=findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title=getString(R.string.registro_quizz)

        txtNombre.requestFocus()
        gradoDAO= GradoDAO(this)
        listaGrados=gradoDAO?.listarGrados()

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                toolbar_layout.isTitleEnabled = true
                toolbar_layout.title = getString(R.string.registro_quizz)

                val params = toolbar.layoutParams
                val newParams: CollapsingToolbarLayout.LayoutParams
                newParams = if (params is CollapsingToolbarLayout.LayoutParams) {
                    params
                } else {
                    CollapsingToolbarLayout.LayoutParams(params)
                }
                newParams.collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
                toolbar.layoutParams = newParams
                toolbar.requestLayout()
            } else {
                toolbar_layout.isTitleEnabled = false
            }

        })


        myUnidadDAO = UnidadDAO(this)
        myQuizDAO = QuizDAO(this)
        max=myQuizDAO?.max()
        myPreguntaDAO = PreguntaDAO(this)

        CantidadQuizz=txtCantidadQuizz
        CantidadDeUnidades=linear_validar
        myListUnidad = myUnidadDAO?.listarUnidades()

        imgSinUnidades.setOnClickListener {
            AgregarUnidActivity.myUnidadObserver=this
            val myIntent=Intent(this,AgregarUnidActivity::class.java)
            myIntent.putExtra("OPERACION", OperacionesCrud.Agregar.toString())
            startActivity(myIntent)
        }

        if(CantidadDeUnidades!=null && myListUnidad!=null) Validador.validarCantidad(linear_validar!!,myListUnidad!!)

        UnidAdapter1.myUnidadObserver = this
        UnidAdapter1.myFilterObserver=this
        myUnidadesAdapter = UnidAdapter1(myListUnidad!!)

        recyUnid.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyUnid.adapter = myUnidadesAdapter

        filtroUnidades.setOnClickListener {
                val misGrados:MutableList<String?> = arrayListOf()

            misGrados.add("Todos")

                listaGrados?.forEach {
                    if(it.descripcion!="Sin Grado")  misGrados.add(it.descripcion)
                }
                val myAlert=AlertDialog.Builder(this)
                myAlert.setTitle("Listado de grados")
                myAlert.setIcon(R.drawable.ic_filter)

                myAlert.setSingleChoiceItems(misGrados.toTypedArray(),-1) { dialogInterface: DialogInterface?, i: Int ->
                    myUnidadesAdapter?.filter?.filter(misGrados[i].toString())
                    categoria.text=misGrados[i]
                    dialogInterface?.dismiss()
                }

                myAlert.show()
        }

        BtnGuardar.setOnClickListener {
            if (validar()) {
                var myQuizzToSave = Asignar()
                if (myQuizDAO?.Insertar(myQuizzToSave)!!) {

                    myQuizzToSave = myQuizDAO?.BuscarQuizz(myQuizzToSave.nombre.toString())!!
                     edtNombre.text?.clear()
                    numUnidad=null
                    chkEstado.isChecked=false

                        myQuizzObserver?.QuizzSaved(myQuizzToSave)
                        myUnidadesAdapter?.notifyDataSetChanged()
                        UnidAdapter1.allowCardChecked=null
                        UnidAdapter1.intChecked=0

                    myListQuizz = myQuizDAO?.ListarQuizNuevos(max!!)
                    myListPregunta = myPreguntaDAO?.ListarPreguntas()
                    if(CantidadDeUnidades!=null && myListUnidad!=null) Validador.validarCantidad(linear_validar!!,myListUnidad!!)



                    AgregarPreguntaActivity.myPreguntaObserver=this
                    myQuizPreguntaAdapter = QuizPreguntaAdapter(myListQuizz!!, myListPregunta!!)
                    recyPreguntas.layoutManager = LinearLayoutManager(this)
                    recyPreguntas.adapter = myQuizPreguntaAdapter
                }
            }
        }

        chkEstado.setOnCheckedChangeListener { _, b ->
            //-1.finalizado, 0.Sin Iniciar, 1.En Progreso
            quizzEstado = if (b) {
                1
            } else
                0
        }

        myListQuizz = myQuizDAO?.ListarQuizNuevos(max!!)
        myListPregunta = myPreguntaDAO?.ListarPreguntas()

        if(CantidadDeUnidades!=null && myListUnidad!=null) Validador.validarCantidad(linear_validar!!,myListUnidad!!)

        AgregarPreguntaActivity.myPreguntaObserver=this
        myQuizPreguntaAdapter = QuizPreguntaAdapter(myListQuizz!!, myListPregunta!!)
        recyPreguntas.layoutManager = LinearLayoutManager(this)
        recyPreguntas.adapter = myQuizPreguntaAdapter
    }

    fun Asignar(): Quiz {
        myQuiz = Quiz()

        myQuiz?.nombre = edtNombre.text.toString()
        myQuiz?.numUnidad = numUnidad.toString()
        myQuiz?.estado=quizzEstado

        return myQuiz!!
    }

    fun validar():Boolean
    {
        if(edtNombre.text?.isEmpty()!!)
        {
            txtNombre.error="El nombre es requerido"
            return false
        }
        else
            txtNombre.error=null

        if(numUnidad==null)
        {
            Toast.makeText(this, "Debe Seleccionar una Unidad", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    override fun startSelection(pos: Int, selected: Boolean) {
        if(selected)
        {
            if (myListUnidad?.size!! >0)
            {
                numUnidad= myListUnidad!![pos].numUnidad
            }
        }
        else
        {
            numUnidad=null
        }
    }

    override fun unidadSaved(myUnidad: Unidad) {

        myListUnidad?.add(myUnidad)
        myUnidadesAdapter?.notifyDataSetChanged()
        if(CantidadDeUnidades!=null && myListUnidad!=null) Validador.validarCantidad(linear_validar!!,myListUnidad!!)
    }

    companion object
    {
        var myQuizzObserver:QuizzObserver?=null
    }

    override fun preguntaSaved(pregunta: Pregunta) {
        myListQuizz = myQuizDAO?.ListarQuizNuevos(max!!)
        myListPregunta = myPreguntaDAO?.ListarPreguntas()
        AgregarPreguntaActivity.myPreguntaObserver=this
        myQuizPreguntaAdapter = QuizPreguntaAdapter(myListQuizz!!, myListPregunta!!)
        recyPreguntas.layoutManager = LinearLayoutManager(this)
        recyPreguntas.adapter = myQuizPreguntaAdapter
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun filterElements(encontrado: Boolean) {
       if(encontrado)
       {
           filtroCantidad.visibility=View.GONE
       }
        else
           filtroCantidad.visibility=View.VISIBLE
    }
}
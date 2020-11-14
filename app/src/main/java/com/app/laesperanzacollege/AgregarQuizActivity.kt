package com.app.laesperanzacollege

import Observers.PreguntaObserver
import Observers.QuizzObserver
import Observers.UnidadObserver
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.QuizPreguntaAdapter
import com.app.laesperanzacollege.adaptadores.UnidAdapter1
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzadao.UnidadDAO
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Quiz
import com.app.laesperanzaedm.model.Unidad
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_agregar_quiz.*
import kotlin.math.abs

class AgregarQuizActivity : AppCompatActivity(),UnidadObserver,PreguntaObserver {
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
    var CantidadDeUnidades:TextView?=null
    var CantidadQuizz:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_quiz)
        var myToolbar=findViewById<Toolbar>(R.id.toolbar)
        myToolbar.title=""
        setSupportActionBar(myToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                toolbar_layout.isTitleEnabled = true
                toolbar_layout.title = getString(R.string.registro_quizz)
            } else {
                toolbar_layout.isTitleEnabled = false
            }

        })

        myUnidadDAO = UnidadDAO(this)
        myQuizDAO = QuizDAO(this)
        max=myQuizDAO?.max()
        myPreguntaDAO = PreguntaDAO(this)

        CantidadQuizz=txtCantidadQuizz
        CantidadDeUnidades=txtcantidadDeUnidades
        myListUnidad = myUnidadDAO?.listarUnidades()

        if(CantidadDeUnidades!=null && myListUnidad!=null) Validador.validarCantidad(CantidadDeUnidades!!,myListUnidad!!)

        UnidAdapter1.myUnidadObserver = this
        myUnidadesAdapter = UnidAdapter1(myListUnidad!!)

        recyUnid.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyUnid.adapter = myUnidadesAdapter

        BtnGuardar.setOnClickListener {
            if (validar()) {
                var myQuizzToSave = Asignar()
                if (myQuizDAO?.Insertar(myQuizzToSave)!!) {

                    myQuizzToSave = myQuizDAO?.BuscarQuizz(myQuizzToSave.nombre.toString())!!

                        //Toast.makeText(this, "Guardado con Exito", Toast.LENGTH_LONG).show()
                       edtNombre.text?.clear()
                    numUnidad=null

                    myQuizzObserver?.QuizzSaved(myQuizzToSave)
                        myUnidadesAdapter?.notifyDataSetChanged()
                        UnidAdapter1.allowCardChecked=null
                        UnidAdapter1.intChecked=0

                    myListQuizz = myQuizDAO?.ListarQuizNuevos(max!!)
                    myListPregunta = myPreguntaDAO?.ListarPreguntas()
                    if(txtCantidadQuizz!=null && myListQuizz!=null) Validador.validarCantidad(txtCantidadQuizz!!,myListQuizz!!)



                    AgregarPreguntaActivity.myPreguntaObserver=this
                    myQuizPreguntaAdapter = QuizPreguntaAdapter(myListQuizz!!, myListPregunta!!)
                    recyPreguntas.layoutManager = LinearLayoutManager(this)
                    recyPreguntas.adapter = myQuizPreguntaAdapter
                }
            }
        }

        myListQuizz = myQuizDAO?.ListarQuizNuevos(max!!)
        myListPregunta = myPreguntaDAO?.ListarPreguntas()

        if(txtCantidadQuizz!=null && myListQuizz!=null) Validador.validarCantidad(txtCantidadQuizz!!,myListQuizz!!)

        AgregarPreguntaActivity.myPreguntaObserver=this
        myQuizPreguntaAdapter = QuizPreguntaAdapter(myListQuizz!!, myListPregunta!!)
        recyPreguntas.layoutManager = LinearLayoutManager(this)
        recyPreguntas.adapter = myQuizPreguntaAdapter
    }

    fun Asignar(): Quiz {
        myQuiz = Quiz()

        myQuiz?.nombre = edtNombre.text.toString()
        myQuiz?.numUnidad = numUnidad.toString()
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
        //nothing to do here
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
}
package com.app.laesperanzacollege

import Observers.FiltroObserver
import Observers.PreguntaObserver
import Observers.QuizzObserver
import Observers.UnidadObserver
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.QuizPreguntaAdapter
import com.app.laesperanzacollege.adaptadores.UnidAdapter1
import com.app.laesperanzadao.GradoDAO
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzadao.UnidadDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Grado
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Quiz
import com.app.laesperanzaedm.model.Unidad
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.activity_agregar_quiz.*
import kotlin.math.abs


class AgregarQuizActivity : AppCompatActivity(),UnidadObserver,PreguntaObserver,FiltroObserver {
    private var myListUnidad: ArrayList<Unidad> = arrayListOf()
    private var myListQuizz: ArrayList<Quiz>? = arrayListOf()
    private var myUnidadDAO: UnidadDAO? = null
    private var myPreguntaDAO: PreguntaDAO? = null
    private var myQuizDAO: QuizDAO? = null
    private var myUnidadesAdapter: UnidAdapter1? = null
    private var mLayoutManager:RecyclerView.LayoutManager?=null
    private var myQuizPreguntaAdapter: QuizPreguntaAdapter? = null
    private var myListPregunta: ArrayList<Pregunta>? = null
    private var myQuiz: Quiz? = null
    private var numUnidad: Int? = null
    private var max: Int? = null
    private var CantidadDeUnidades:LinearLayoutCompat?=null
    private var CantidadQuizz:TextView?=null
    private var CantidadQuizView:LinearLayoutCompat?=null
    private var quizzEstado:Int=0 //Por defecto sin inciar
    private var gradoDAO:GradoDAO?=null
    private var listaGrados:ArrayList<Grado>?=null
    private var mOperacion:String?=null
    private var quizToEdit:Quiz?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_quiz)
        val myToolbar=findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title=getString(R.string.registro_quizz)

        //txtNombre.requestFocus()
        gradoDAO= GradoDAO(this)
        listaGrados=gradoDAO?.listarGrados()

        mOperacion=intent.extras?.get(getString(R.string.txt_operacion)).toString()
        val extras=intent.extras?.get(getString(R.string.keyNameQuiz))

        if(extras!=null)
            quizToEdit=extras as Quiz

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val nestedScroolParams = itemsQuizzes.layoutParams as CoordinatorLayout.LayoutParams
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
                //toolbar.requestLayout()

                nestedScroolParams.setMargins(3, 3, 3, 0)
            } else {
                toolbar_layout.isTitleEnabled = false
                nestedScroolParams.setMargins(3, 70, 3, 0)
            }
            itemsQuizzes.layoutParams = nestedScroolParams
        })

        myUnidadDAO = UnidadDAO(this)
        myQuizDAO = QuizDAO(this)
        max=myQuizDAO?.max()
        myPreguntaDAO = PreguntaDAO(this)

        CantidadQuizz=txtCantidadQuizz
        CantidadDeUnidades=linear_validar
        CantidadQuizView=txtCantidadQuizzview

        myListUnidad = myUnidadDAO?.listarUnidades()!!
        RespuestaActivity.mPreguntaObserver=this

        //setear los campos que se van a poder editar
        if(mOperacion==OperacionesCrud.Editar.toString())
        {
            //asignar dinamicamente el texto que tendra el textview cuando no hayan datos
            txtCantidadQuizz.text=getString(R.string.sin_datos,getString(R.string.preguntas))

            edtNombre.setText(quizToEdit?.nombre)
            edtPuntaje.setText(quizToEdit?.puntaje.toString())
            quizzEstado= quizToEdit?.estado!!

            val pos= myListUnidad.indexOfFirst { x->x.numUnidad==quizToEdit?.numUnidad}

            if(pos!=-1)
            {
                myUnidadesAdapter = UnidAdapter1(myListUnidad,pos)
            }

            chkEstado.visibility=View.GONE
        }
        else
        {
            //asignar dinamicamente el texto que tendra el textview cuando no hayan datos
            txtCantidadQuizz.text=getString(R.string.sin_datos,getString(R.string.txt_quizzes))
            myUnidadesAdapter = UnidAdapter1(myListUnidad,-1)
        }

        edtPuntaje.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
                //
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                ///
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if(s.isNotEmpty() && Integer.parseInt(s.toString())>100) {
                        val mSnack= Utils.crearCustomSnackbar(
                            viewPrin, Color.RED, android.R.drawable.stat_notify_error,"Nota no debe ser mayor a 100", layoutInflater)
                        mSnack.show()
                    }
                }
            }
        })
        imgSinUnidades.setOnClickListener {
            AgregarUnidActivity.myUnidadObserver=this
            val myIntent=Intent(this,AgregarUnidActivity::class.java)
            myIntent.putExtra("OPERACION", OperacionesCrud.Agregar.toString())
            startActivity(myIntent)
        }

        UnidAdapter1.myUnidadObserver = this
        UnidAdapter1.myFilterObserver=this
        mLayoutManager=LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyUnid.layoutManager = mLayoutManager
        recyUnid.adapter = myUnidadesAdapter
        if(CantidadDeUnidades!=null) Validador.validarCantidad(linear_validar!!, myListUnidad)

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
                var myQuizzToSave = asignar()

                if(mOperacion==OperacionesCrud.Agregar.toString())
                {
                    if (myQuizDAO?.insertar(myQuizzToSave)!!) {

                        myQuizzToSave = myQuizDAO?.buscarQuizz(myQuizzToSave.nombre.toString())!!

                        myQuizzObserver?.QuizzSaved(myQuizzToSave)
                        myUnidadesAdapter?.notifyDataSetChanged()

                        myListQuizz = myQuizDAO?.listarQuizNuevos(max!!)
                        myListPregunta = myPreguntaDAO?.ListarPreguntas()

                        limpiarControles()

                        if(myListPregunta!=null && CantidadQuizView!=null) Validador.validarCantidad(CantidadQuizView!!,myListQuizz!!)

                        AgregarPreguntaActivity.myPreguntaObserver=this
                        myQuizPreguntaAdapter = QuizPreguntaAdapter(myListQuizz!!, myListPregunta!!,
                            mOperacion!!
                        )
                        recyPreguntas.layoutManager = LinearLayoutManager(this)
                        recyPreguntas.adapter = myQuizPreguntaAdapter
                    }
                }
                else
                {
                    myQuizzToSave.quizId=quizToEdit?.quizId

                    if(myQuizDAO?.actualizar(myQuizzToSave)!!)
                    {
                        limpiarControles()
                        myQuizzObserver?.QuizzSaved(myQuizzToSave)
                        myUnidadesAdapter?.notifyDataSetChanged()
                        this.finish()
                    }
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

        if(mOperacion==OperacionesCrud.Agregar.toString())
        {
            myListQuizz = myQuizDAO?.listarQuizNuevos(max!!)
            myListPregunta = myPreguntaDAO?.ListarPreguntas()
            myQuizPreguntaAdapter = QuizPreguntaAdapter(myListQuizz!!, myListPregunta!!,
                mOperacion!!
            )
            recyPreguntas.layoutManager = LinearLayoutManager(this)
            recyPreguntas.adapter = myQuizPreguntaAdapter
        }
        else
        {
            BtnGuardar.setImageResource(R.drawable.ic_save)

            myListPregunta = myPreguntaDAO?.ListarPreguntas(quizToEdit?.quizId!!)
            myListQuizz?.add(quizToEdit!!)
            numUnidad=quizToEdit?.numUnidad

            myQuizPreguntaAdapter = QuizPreguntaAdapter(myListQuizz!!, myListPregunta!!,
                mOperacion!!
            )
            recyPreguntas.layoutManager = LinearLayoutManager(this)
            recyPreguntas.adapter = myQuizPreguntaAdapter
        }

        if(myListPregunta!=null && CantidadQuizView!=null) Validador.validarCantidad(CantidadQuizView!!,myListQuizz!!)
    }

    private fun asignar(): Quiz {
        myQuiz = Quiz()

        myQuiz?.nombre = edtNombre.text.toString()
        myQuiz?.numUnidad = numUnidad
        myQuiz?.estado=quizzEstado
        myQuiz?.puntaje=Integer.parseInt(edtPuntaje.text.toString())

        return myQuiz!!
    }

    private fun validar():Boolean
    {
        if(edtNombre.text?.isEmpty()!!)
        {
            txtNombre.error="El nombre es requerido"
            txtNombre.setHelperTextColor(ColorStateList.valueOf(Color.RED))
            return false
        }
        else
        {
            txtNombre.error=null
            txtNombre.helperText=null
        }

        if(edtPuntaje.text.isEmpty())
        {
            val mSnack= Utils.crearCustomSnackbar(
                viewPrin, Color.RED, android.R.drawable.stat_notify_error, "Puntaje es requerido", layoutInflater
            )
            mSnack.show()
            return false
        }


        if(numUnidad==null)
        {
            val mSnack= Utils.crearCustomSnackbar(
                viewPrin, Color.RED, android.R.drawable.stat_notify_error,
                getString(R.string.error_incomplete_data,getString(R.string.txt_unidad)), layoutInflater
            )
            mSnack.show()
            return false
        }

        return true
    }

    override fun startSelection(pos: Int, selected: Boolean) {
        if(selected)
        {
            if (myListUnidad.size >0)
            {
                numUnidad= myListUnidad[pos].numUnidad
            }
        }
        else
        {
            numUnidad=null
        }
    }

    override fun unidadSaved(myUnidad: Unidad) {

        myListUnidad.add(myUnidad)
        myUnidadesAdapter?.notifyDataSetChanged()
        if(CantidadDeUnidades!=null) Validador.validarCantidad(linear_validar!!, myListUnidad)
    }

    companion object
    {
        var myQuizzObserver:QuizzObserver?=null
    }

    override fun preguntaSaved(pregunta: Pregunta) {
        myListPregunta?.add(pregunta)
        myQuizPreguntaAdapter?.notifyDataSetChanged()
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

    private fun limpiarControles()
    {
        edtNombre.text?.clear()
        edtPuntaje.text?.clear()
        numUnidad=null
        chkEstado.isChecked=false

        UnidAdapter1.allowCardChecked=null
        UnidAdapter1.intChecked=0
    }

    override fun onDestroy() {
        UnidAdapter1.allowCardChecked=null
        UnidAdapter1.intChecked=0
        super.onDestroy()
    }
}
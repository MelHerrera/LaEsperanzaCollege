package com.app.laesperanzacollege

import Observers.QuizesAdapterObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.laesperanzacollege.adaptadores.QuizzesAdapter
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzadao.enums.TipoDeUsuarios
import com.app.laesperanzadao.enums.TipodeTest
import com.app.laesperanzaedm.model.Quiz
import com.app.laesperanzaedm.model.Usuario
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_listar_quizzes.*

class ListarQuizzesActivity : AppCompatActivity(),QuizesAdapterObserver {
    private var keyNameUser=""
    private var keyNameTest=""
    private var myListQuizzesAdapter:QuizzesAdapter?=null
    private var myListQuizzes:ArrayList<Quiz> = arrayListOf()
    private var myQuizDAO:QuizDAO?=null
    private var estudiante:Usuario= Usuario()
    private var myOperacion:TipodeTest?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_quizzes)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        keyNameUser=getString(R.string.keyNameUser)
        keyNameTest=getString(R.string.txt_tipoTest)

        estudiante=intent.extras?.get(keyNameUser) as Usuario
        myOperacion=intent.extras?.get(keyNameTest) as TipodeTest

        //asignar dinamicamente el texto que tendra el textview cuando no hayan datos
        txtCantPruebaQuizzes.text=getString(R.string.sin_datos,getString(R.string.prueba))

        myQuizDAO= QuizDAO(this)

        if(myOperacion!=null)
        {
            myListQuizzes = when(myOperacion!!) {
                TipodeTest.Prueba-> {
                    myQuizDAO!!.listarQuizzesPrueba(estudiante.codGrado,this)
                }
                TipodeTest.Practica-> {
                    myQuizDAO!!.listarQuizzesDePractica(estudiante.codGrado,this)
                }
            }
        }

        if(txtCantPruebaQuizzes!=null) Validador.validarCantidad(viewCantPruebaQuizzes!!, myListQuizzes)

        MainQuizFragment.mQuizesAdapterObserver=this
        myListQuizzesAdapter= QuizzesAdapter(myListQuizzes,TipoDeUsuarios.Estudiante,estudiante.id!!, myOperacion!!)


        recy_ls.layoutManager= FlexboxLayoutManager(this)
        recy_ls.adapter=myListQuizzesAdapter
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun updateRecyClerQuiz() {
        myListQuizzes=myQuizDAO!!.listarQuizzesPrueba(estudiante.codGrado,this)
        myListQuizzesAdapter= QuizzesAdapter(myListQuizzes,TipoDeUsuarios.Estudiante,estudiante.id!!,myOperacion!!)
        recy_ls.adapter=myListQuizzesAdapter
        if(txtCantPruebaQuizzes!=null) Validador.validarCantidad(viewCantPruebaQuizzes!!, myListQuizzes)
    }
}
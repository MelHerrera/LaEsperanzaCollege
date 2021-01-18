package com.app.laesperanzacollege

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.QuizzesAdapter
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzadao.enums.TipoDeUsuarios
import com.app.laesperanzadao.enums.TipodeTest
import com.app.laesperanzaedm.model.Quiz
import com.app.laesperanzaedm.model.Usuario
import com.app.laesperanzacollege.Utils.Companion.spanCalc
import kotlinx.android.synthetic.main.activity_listar_quizzes.*
import kotlinx.android.synthetic.main.activity_quizzes.*
import kotlinx.android.synthetic.main.activity_unidades.*

class ListarQuizzesActivity : AppCompatActivity() {
    private var keyNameUser=""
    private var keyNameTest=""
    private var myListQuizzesAdapter:QuizzesAdapter?=null
    private var myLayoutManager:RecyclerView.LayoutManager?=null
    private var myListQuizzes:ArrayList<Quiz> = arrayListOf()
    private var myQuizDAO:QuizDAO?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_quizzes)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        keyNameUser=getString(R.string.keyNameUser)
        keyNameTest=getString(R.string.txt_tipoTest)

        val estudiante: Usuario?=intent.extras?.get(keyNameUser) as Usuario
        val myOperacion:TipodeTest=intent.extras?.get(keyNameTest) as TipodeTest

        //asignar dinamicamente el texto que tendra el textview cuando no hayan datos
        txtCantPruebaQuizzes.text=getString(R.string.sin_datos,getString(R.string.txt_quizzes))

        myQuizDAO= QuizDAO(this)

        myListQuizzes = when(myOperacion) {
            TipodeTest.Prueba-> {
                myQuizDAO!!.listarQuizzesPrueba(estudiante?.codGrado)
            }
            TipodeTest.Practica-> {
                myQuizDAO!!.listarQuizzesDePractica(estudiante?.codGrado)
            }
        }

        if(txtCantPruebaQuizzes!=null) Validador.validarCantidad(viewCantPruebaQuizzes!!,myListQuizzes!!)

        myListQuizzesAdapter= QuizzesAdapter(myListQuizzes,TipoDeUsuarios.Estudiante,estudiante?.id!!)


        val newSpan=spanCalc(recy_ls,this@ListarQuizzesActivity)
        if(recy_ls!=null && newSpan>0)
        {
            recy_ls.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        recy_ls.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        myLayoutManager=GridLayoutManager(this@ListarQuizzesActivity,newSpan)
                        recy_ls.layoutManager=myLayoutManager
                    }
                })
        }
        else
            myLayoutManager=GridLayoutManager(this,2)

        recy_ls.layoutManager=myLayoutManager
        recy_ls.adapter=myListQuizzesAdapter
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
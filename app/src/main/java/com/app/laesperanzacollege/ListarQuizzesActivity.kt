package com.app.laesperanzacollege

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.QuizzesAdapter
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzadao.enums.TipoDeUsuarios
import com.app.laesperanzadao.enums.TipodeTest
import com.app.laesperanzaedm.model.Quiz
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.activity_listar_quizzes.*

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

        myQuizDAO= QuizDAO(this)

        myListQuizzes = when(myOperacion) {
            TipodeTest.Prueba-> {
                myQuizDAO!!.listarQuizzesPrueba(estudiante?.codGrado)
            }
            TipodeTest.Practica-> {
                myQuizDAO!!.listarQuizzesDePractica(estudiante?.codGrado)
            }
        }


        myLayoutManager=GridLayoutManager(this,2)
        myListQuizzesAdapter= QuizzesAdapter(myListQuizzes,TipoDeUsuarios.Estudiante)
        recy_ls.layoutManager=myLayoutManager
        recy_ls.adapter=myListQuizzesAdapter
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
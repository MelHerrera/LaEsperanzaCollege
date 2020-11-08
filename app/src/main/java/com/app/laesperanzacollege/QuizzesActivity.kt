package com.app.laesperanzacollege

import Observers.QuizzObserver
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.QuizzesAdapter
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzaedm.model.Quiz
import kotlinx.android.synthetic.main.activity_quizzes.*

class QuizzesActivity : AppCompatActivity(),QuizzObserver {
    var myQuizzesAdapter:QuizzesAdapter?=null
    var myLayoutManager:RecyclerView.LayoutManager?=null
    var myListQuizzes:ArrayList<Quiz>?=null
    var myQuizDAO:QuizDAO?=null
    var txtCantQuizzes:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizzes)

        myQuizDAO= QuizDAO(this)
        myLayoutManager=GridLayoutManager(this,2)

        txtCantQuizzes=txtCantQuizzes
        myListQuizzes=myQuizDAO?.ListarQuizzes()

        if(txtCantQuizzes!=null && myListQuizzes!=null) Validador.validarCantidad(txtCantQuizzes!!,myListQuizzes!!)

        AgregarQuizActivity.myQuizzObserver=this
        myQuizzesAdapter= QuizzesAdapter(myListQuizzes!!)

        recyQuizzes.layoutManager=myLayoutManager
        recyQuizzes.adapter=myQuizzesAdapter

        btnAgregarQuiz.setOnClickListener {

            startActivity(Intent(this,AgregarQuizActivity::class.java))
        }
    }

    override fun QuizzSaved(quizz: Quiz) {
        myListQuizzes?.add(quizz)
        myQuizzesAdapter?.notifyDataSetChanged()
        if(txtCantQuizzes!=null && myListQuizzes!=null) Validador.validarCantidad(txtCantQuizzes!!,myListQuizzes!!)
    }
}
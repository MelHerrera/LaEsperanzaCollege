package com.app.laesperanzacollege

import Observers.QuizzObserver
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.QuizzesAdapter
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzaedm.model.Quiz
import kotlinx.android.synthetic.main.activity_quizzes.*

class QuizzesActivity : AppCompatActivity(),QuizzObserver {
    private var myQuizzesAdapter:QuizzesAdapter?=null
    private var myLayoutManager:RecyclerView.LayoutManager?=null
    private var myListQuizzes:ArrayList<Quiz>?=null
    private var myQuizDAO:QuizDAO?=null
    private var txtCantQuizzes:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizzes)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        //validar que no se pueda poner en progreso cuando el quiz no tenga preguntas aun
        myQuizDAO= QuizDAO(this)
        myLayoutManager=GridLayoutManager(this,2)

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
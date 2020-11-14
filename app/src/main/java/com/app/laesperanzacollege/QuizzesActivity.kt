package com.app.laesperanzacollege

import Observers.QuizzObserver
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.QuizzesAdapter
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzaedm.model.Quiz
import kotlinx.android.synthetic.main.activity_quizzes.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuizzesActivity : AppCompatActivity(),QuizzObserver {
    private var myQuizzesAdapter:QuizzesAdapter?=null
    private var myLayoutManager:RecyclerView.LayoutManager?=null
    private var myListQuizzes:ArrayList<Quiz>?=null
    private var myQuizDAO:QuizDAO?=null
    private var txtCantQuizzes:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizzes)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title=getString(R.string.txt_quizzes)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_estudiante,menu)

        val search = menu?.findItem(R.id.app_bar_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Buscar"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                GlobalScope.launch {
                    val myQuizSearched=myQuizzesAdapter?.filterItem(newText.toString())

                    if(myQuizSearched==null && newText?.length!! >0)
                    {
                        resultados.visibility= View.VISIBLE
                    }
                    else
                    {
                        resultados.visibility= View.GONE
                        if (myQuizSearched != null) {
                            myQuizzesAdapter?.getItemViewType(myListQuizzes!!.indexOf(myQuizSearched))
                        }
                    }
                }
                return true
            }
        })
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
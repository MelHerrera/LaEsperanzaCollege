package com.app.laesperanzacollege

import Observers.QuizzObserver
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.QuizzesAdapter
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzadao.enums.TipoDeUsuarios
import com.app.laesperanzaedm.model.Quiz
import kotlinx.android.synthetic.main.activity_quizzes.*
import kotlinx.android.synthetic.main.activity_unidades.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow


class QuizzesActivity : AppCompatActivity(),QuizzObserver {
    private var myQuizzesAdapter:QuizzesAdapter?=null
    private var myLayoutManager:RecyclerView.LayoutManager?=null
    private var myListQuizzes:ArrayList<Quiz>?=null
    private var myQuizDAO:QuizDAO?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizzes)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myQuizDAO= QuizDAO(this)

        recyQuizzes.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    recyQuizzes.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val newSpanCount=spanCalc()
                    myLayoutManager=GridLayoutManager(this@QuizzesActivity,newSpanCount)
                    recyQuizzes.layoutManager=myLayoutManager
                }
            })

        myListQuizzes=myQuizDAO?.ListarQuizzes()
        //asignar dinamicamente el texto que tendra el textview cuando no hayan datos
        txtCantQuizzes.text=getString(R.string.sin_datos,getString(R.string.txt_quizzes))

        if(txtCantQuizzes!=null && myListQuizzes!=null) Validador.validarCantidad(viewCantQuizzes!!,myListQuizzes!!)

        AgregarQuizActivity.myQuizzObserver=this
        myQuizzesAdapter= QuizzesAdapter(myListQuizzes!!,TipoDeUsuarios.Admin,-1)

        myLayoutManager=GridLayoutManager(this,2)
        recyQuizzes.layoutManager=myLayoutManager
        recyQuizzes.adapter=myQuizzesAdapter

        btnAgregarQuiz.setOnClickListener {

            startActivity(Intent(this,AgregarQuizActivity::class.java))
        }
    }

    override fun QuizzSaved(quizz: Quiz) {
        myListQuizzes?.add(quizz)
        myQuizzesAdapter?.notifyDataSetChanged()
        if(txtCantQuizzes!=null && myListQuizzes!=null) Validador.validarCantidad(viewCantQuizzes!!,myListQuizzes!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_estudiante,menu)

        val search = menu?.findItem(R.id.app_bar_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = getString(R.string.txt_buscar)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                myQuizzesAdapter?.filter?.filter(newText.toString().toUpperCase(Locale.ROOT))
                return true
            }
        })
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun spanCalc():Int
    {
        val viewWidth: Int = recyQuizzes.width
        val cardViewWidth: Float =resources.getDimension(R.dimen.card_quizzes)
        return Utils.floorDiv(viewWidth,cardViewWidth.toInt())
    }
}
package com.app.laesperanzacollege

import Observers.QuizzObserver
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import com.app.laesperanzacollege.adaptadores.QuizzesAdapter
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzadao.enums.TipoDeUsuarios
import com.app.laesperanzadao.enums.TipodeTest
import com.app.laesperanzaedm.model.Quiz
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_quizzes.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*


class QuizzesActivity : AppCompatActivity(),QuizzObserver, ActionMode.Callback {
    private var myQuizzesAdapter:QuizzesAdapter?=null
    private var myListQuizzes:ArrayList<Quiz>?=null
    private var myQuizDAO:QuizDAO?=null
    private var mListQuizSelected:ArrayList<Quiz>?= arrayListOf()
    private var mActionMode:ActionMode?=null
    private var mActionModeMenu:Menu?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizzes)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myQuizDAO= QuizDAO(this)

        myListQuizzes=myQuizDAO?.listarQuizzes()
        //asignar dinamicamente el texto que tendra el textview cuando no hayan datos
        txtCantQuizzes.text=getString(R.string.sin_datos,getString(R.string.txt_quizzes))

        if(txtCantQuizzes!=null && myListQuizzes!=null) Validador.validarCantidad(viewCantQuizzes!!,myListQuizzes!!)

        AgregarQuizActivity.myQuizzObserver=this
        QuizzesAdapter.myQuizzObserver=this
        myQuizzesAdapter= QuizzesAdapter(myListQuizzes!!,TipoDeUsuarios.Admin,-1,TipodeTest.Practica)

        //myLayoutManager=GridLayoutManager(this,2)
        recyQuizzes.layoutManager= FlexboxLayoutManager(this)
        recyQuizzes.adapter=myQuizzesAdapter

        btnAgregarQuiz.setOnClickListener {
            val mIntent=Intent(this,AgregarQuizActivity::class.java)
            mIntent.putExtra(getString(R.string.txt_operacion),OperacionesCrud.Agregar)
            startActivity(mIntent)
        }
    }

    override fun QuizzSaved(quizz: Quiz) {
        val pos=myListQuizzes?.indexOfFirst { it.quizId==quizz.quizId}

        if(pos!=null && pos!=-1)
            myListQuizzes!![pos]=quizz
        else
        myListQuizzes?.add(quizz)

        myQuizzesAdapter?.notifyDataSetChanged()
        if(txtCantQuizzes!=null && myListQuizzes!=null) Validador.validarCantidad(viewCantQuizzes!!,myListQuizzes!!)
    }

    override fun actionModeInit() {

        if(mListQuizSelected?.size!!>0)
        {
            if (mActionMode == null) {
                mActionMode = startSupportActionMode(this)
            }
            else
            {
                val menuItemEdit=mActionModeMenu?.findItem(R.id.myItemEditQuiz)
                menuItemEdit?.isVisible = mListQuizSelected?.size!! <= 1

                mActionMode?.title="${mListQuizSelected?.size}"
            }
        }
        else
            mActionMode?.finish()
    }

    override fun quizSelection(pos: Int, selected: Boolean) {
        if(selected)
        {
            mListQuizSelected?.add(myListQuizzes?.get(pos)!!)
        }
        else
        {
            mListQuizSelected?.remove(myListQuizzes?.get(pos))
        }
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
       super.onBackPressed()
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when(item?.itemId)
        {
            R.id.myItemDeleteQuiz->
            {
                if(mListQuizSelected?.size!! >0)
                {
                    val myAlert= AlertDialog.Builder(this@QuizzesActivity)

                    myAlert.setMessage("¿Estás Seguro que Deseas Eliminar?")
                    myAlert.setTitle("Confirmar")
                    myAlert.setIcon(android.R.drawable.ic_menu_delete)

                    myAlert.setPositiveButton("Si") { _, _ ->

                        for (it in mListQuizSelected!!) {
                            if(QuizDAO(this@QuizzesActivity).eliminarQuiz(it.quizId))
                                myListQuizzes?.remove(it)
                        }
                        myQuizzesAdapter?.notifyDataSetChanged()
                        mListQuizSelected= arrayListOf()
                        mode?.finish()
                        if(txtCantQuizzes!=null && myListQuizzes!=null) Validador.validarCantidad(viewCantQuizzes!!,myListQuizzes!!)
                    }

                    myAlert.setNegativeButton("No") { _, _ ->
                        myAlert.create().dismiss()
                        myQuizzesAdapter?.notifyDataSetChanged()
                        mListQuizSelected= arrayListOf()
                        mode?.finish()
                        if(txtCantQuizzes!=null && myListQuizzes!=null) Validador.validarCantidad(viewCantQuizzes!!,myListQuizzes!!)
                    }

                    myAlert.show()
                }
            }
            R.id.myItemEditQuiz->
            {
                val mIntent=Intent(this@QuizzesActivity,AgregarQuizActivity::class.java)
                mIntent.putExtra(getString(R.string.txt_operacion),OperacionesCrud.Editar)
                mIntent.putExtra(getString(R.string.keyNameQuiz), mListQuizSelected?.get(0))
                startActivity(mIntent)
                myQuizzesAdapter?.notifyDataSetChanged()
                mListQuizSelected= arrayListOf()
                mode?.finish()

                GlobalScope.launch {
                    this@QuizzesActivity.finish()
                }
            }
            else->
                return false
        }
        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_quiz,menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        if(mListQuizSelected?.size!!>0)
            mode?.title="${mListQuizSelected?.size}"

        mActionModeMenu=menu
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        mActionMode=null
        mActionModeMenu=null
        myQuizzesAdapter?.notifyDataSetChanged()
        mListQuizSelected= arrayListOf()
        if(txtCantQuizzes!=null && myListQuizzes!=null) Validador.validarCantidad(viewCantQuizzes!!,myListQuizzes!!)
    }
}
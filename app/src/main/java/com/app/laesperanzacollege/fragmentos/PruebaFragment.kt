package com.app.laesperanzacollege.fragmentos

import Observers.MainViewPagerObserver
import Observers.ViewPagerObserver
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.app.laesperanzacollege.MainQuizFragment
import com.app.laesperanzacollege.R
import com.app.laesperanzacollege.TestActivity
import com.app.laesperanzacollege.adaptadores.QuizPageAdapter
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzadao.enums.TipodeTest
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Quiz
import com.app.laesperanzaedm.model.UsuarioQuiz
import kotlinx.android.synthetic.main.fragment_prueba.view.*

class PruebaFragment : Fragment(),ViewPagerObserver {
    var quiz:Quiz?=null
    private var usuarioId:Int=-1
    private var usuarioQuiz:UsuarioQuiz?=null
    var pagePos:Int=0
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: QuizPageAdapter
    private lateinit var listPreguntas: ArrayList<Pregunta>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myView = inflater.inflate(R.layout.fragment_prueba, container, false)

        val myPreguntaDAO = PreguntaDAO(myView.context)
        setHasOptionsMenu(true)
        TestActivity.myViewPagerObserver=this

        quiz = arguments?.get(getString(R.string.keyNameQuiz)) as Quiz?
        usuarioId = arguments?.get(getString(R.string.keyNameUser)) as Int
        usuarioQuiz = arguments?.get(getString(R.string.keynameUsuarioQuiz)) as UsuarioQuiz
        val mOpe=arguments?.get(getString(R.string.txt_tipoTest)) as TipodeTest

        viewPager = myView.quizPager
        listPreguntas = myPreguntaDAO.ListarPreguntas(quiz?.quizId!!)

        if (listPreguntas.size > 0) {
            MainQuizFragment.myViewPagerObserver = this
            adapter = QuizPageAdapter(this, listPreguntas, usuarioId, usuarioQuiz?.UsuarioQuizId!!, quiz!!,mOpe)
            viewPager.adapter = adapter
        }
        viewPager.isUserInputEnabled = false
        retainInstance = false

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                pagePos=position
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                if(state==ViewPager2.SCROLL_STATE_IDLE)
                {
                    if (mainPagerObserver != null) {
                        if (mainPagerObserver!!.estaEnRevision()) {
                            mainPagerObserver?.mostrarRespuestas(pagePos)
                        }
                    }
                }
                super.onPageScrollStateChanged(state)
            }
        })

        return myView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_test,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.itemSalir->
            {
                if (mainPagerObserver != null) {
                    if (mainPagerObserver!!.estaEnRevision()) {
                       activity?.finish()
                    }
                    else
                    {
                        val myAlert = AlertDialog.Builder(context!!)
                        myAlert.setIcon(android.R.drawable.ic_dialog_alert)
                        myAlert.setTitle("Abandonar Prueba")
                        myAlert.setMessage("Si Sale de la Prueba Perdera el avance. ¿Esta Seguro que desea Abandonar la Prueba?")
                        myAlert.setNegativeButton(getString(R.string.negativa)) { _, _ ->
                        }

                        myAlert.setPositiveButton("Si") { _, _ ->
                            activity?.finish()
                        }

                        myAlert.show()
                    }
                }

            }
            else->
                Toast.makeText(context,"Nada Se Selecciono",Toast.LENGTH_LONG).show()
        }
        return false
    }

    override fun paginaSiguiente() {
        viewPager.currentItem= viewPager.currentItem+1
    }

    override fun paginaAnterior() {
        viewPager.currentItem= viewPager.currentItem-1
    }

    override fun paginaPrimera():Int {
        viewPager.currentItem=0
        return viewPager.adapter?.itemCount!!
    }

    override fun estaEnPaginaPrimera(): Boolean {
        return viewPager.currentItem==0
    }

    companion object
    {
        var mainPagerObserver:MainViewPagerObserver?=null
    }
}
package com.app.laesperanzacollege.fragmentos

import Observers.ViewPagerObserver
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.app.laesperanzacollege.LoginActivity
import com.app.laesperanzacollege.MainQuizFragment
import com.app.laesperanzacollege.Preferencias
import com.app.laesperanzacollege.R
import com.app.laesperanzacollege.adaptadores.QuizPageAdapter
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Quiz
import kotlinx.android.synthetic.main.fragment_prueba.view.*

class PruebaFragment : Fragment(),ViewPagerObserver {
    var quiz:Quiz?=null
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: QuizPageAdapter
    private lateinit var listPreguntas: ArrayList<Pregunta>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myView=inflater.inflate(R.layout.fragment_prueba, container, false)

        val myPreguntaDAO=PreguntaDAO(myView.context)
        setHasOptionsMenu(true)

        quiz=arguments?.get(getString(R.string.keyNameUser)) as Quiz?
        viewPager = myView.quizPager
        listPreguntas=myPreguntaDAO.ListarPreguntas(quiz?.quizId!!)

        if(listPreguntas.size>0)
        {
            MainQuizFragment.myViewPagerObserver=this
            adapter= QuizPageAdapter(this,listPreguntas)
            viewPager.adapter=adapter
        }
        viewPager.isUserInputEnabled = false
        return myView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_test,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
           /* R.id.itemCompartir->
            {
                Toast.makeText(context,"Compartir",Toast.LENGTH_LONG).show()
            }
            R.id.itemFavoritos->
            {
                Toast.makeText(context,"Favoritos",Toast.LENGTH_LONG).show()
            }*/
            R.id.itemSalir->
            {
                val myAlert = AlertDialog.Builder(context!!)
                myAlert.setTitle("Abandonar Prueba")
                myAlert.setMessage("Si Sale de la Prueba Perdera el avance. ¿Esta Seguro que desea Abandonar la Prueba?")
                myAlert.setNegativeButton(getString(R.string.no)) { _, _ ->
                }

                myAlert.setPositiveButton("Si") { _, _ ->
                    activity?.finish()
                }

                myAlert.show()
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
}
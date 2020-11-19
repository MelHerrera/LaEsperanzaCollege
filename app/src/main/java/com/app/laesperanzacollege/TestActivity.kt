package com.app.laesperanzacollege

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.app.laesperanzacollege.adaptadores.QuizPageAdapter
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Quiz
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : FragmentActivity() {
    var quiz:Quiz?=null
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: QuizPageAdapter
    private lateinit var listPreguntas: ArrayList<Pregunta>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        var myPreguntaDAO=PreguntaDAO(this)

        quiz= intent.extras?.get(getString(R.string.keyNameUser)) as Quiz?
        viewPager = quizPager
        listPreguntas=myPreguntaDAO.ListarPreguntas(quiz?.quizId!!)

        if(listPreguntas.size>0)
        {
            adapter= QuizPageAdapter(this,listPreguntas)
            viewPager.adapter=adapter
        }


    }
}
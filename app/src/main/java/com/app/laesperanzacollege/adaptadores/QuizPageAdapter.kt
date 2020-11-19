package com.app.laesperanzacollege.adaptadores

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.laesperanzacollege.MainQuizFragment
import com.app.laesperanzaedm.model.Pregunta

class QuizPageAdapter(frag: FragmentActivity,private var listPregunta: ArrayList<Pregunta>):FragmentStateAdapter(frag) {
    override fun getItemCount(): Int {
       return listPregunta.size
    }

    override fun createFragment(position: Int): Fragment {
        val myFragmentQuiz=MainQuizFragment(listPregunta[position],position+1,listPregunta.size)

        return myFragmentQuiz
    }
}
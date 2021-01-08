package com.app.laesperanzacollege.adaptadores

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.laesperanzacollege.MainQuizFragment
import com.app.laesperanzaedm.model.Pregunta

class QuizPageAdapter(frag:Fragment,var listPregunta: ArrayList<Pregunta>, var usuarioId:Int,var usuarioQuizId:Int):FragmentStateAdapter(frag) {
    override fun getItemCount(): Int {
       return listPregunta.size
    }

    override fun createFragment(position: Int): Fragment {
        return MainQuizFragment(listPregunta[position],position+1, listPregunta.size, usuarioId,usuarioQuizId)
    }
}
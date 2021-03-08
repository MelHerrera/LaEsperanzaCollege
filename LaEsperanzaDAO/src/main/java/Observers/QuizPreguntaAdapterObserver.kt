package Observers

import com.app.laesperanzaedm.model.Pregunta

interface QuizPreguntaAdapterObserver {
    fun deleteRecyItem(item: Pregunta)
}
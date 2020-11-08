package Observers

import com.app.laesperanzaedm.model.Pregunta

interface PreguntaObserver {
    fun preguntaSaved(pregunta: Pregunta)
}
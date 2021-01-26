package Observers

import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Respuesta

interface RespuestaObserver {

    fun respuestaSaved(newRespuestas:ArrayList<Respuesta>)
    fun preguntaSaved(pregunta: Pregunta, mListRespuestas:ArrayList<Respuesta>)
}
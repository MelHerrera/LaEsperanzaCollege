package Observers

import com.app.laesperanzaedm.model.Respuesta

interface RespuestaObserver {

    fun respuestaSaved(respuesta: Respuesta)
}
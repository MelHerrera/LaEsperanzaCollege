package Observers

import com.app.laesperanzaedm.model.Unidad

interface UnidadObserver {
    fun startSelection(pos:Int,selected:Boolean)
    fun unidadSaved(myUnidad:Unidad)
}
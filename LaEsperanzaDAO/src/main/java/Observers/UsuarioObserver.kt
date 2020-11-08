package Observers

import com.app.laesperanzaedm.model.Usuario

interface UsuarioObserver {
    fun usuarioSaved(myUsuario:Usuario)
    fun usuarioRemoved(posicion:Int)
}
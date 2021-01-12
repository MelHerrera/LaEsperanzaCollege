package Observers

import com.app.laesperanzaedm.model.Usuario

interface UsuarioObserver {
    fun actualizarUsuario(mUsuario:Usuario)
    fun agregarUsuario(mUsuario:Usuario)
}
package com.app.laesperanzacollege

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var keyName=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        keyName=getString(R.string.keyNameUser)

        //Borrar cuando este en produccion
        edtusuario.setText("Hamador")
        edtcontra.setText("admin2020")

        btnsesion.setOnClickListener {

            if(verificarErrores())
            {
                val usuarioDAO:UsuarioDAO?= UsuarioDAO(this)
                val myUsuario:Usuario?=usuarioDAO?.Buscar(edtusuario.text.toString(),edtcontra.text.toString())

                if(myUsuario!=null)
                {
                    if(myUsuario.id!! >0)
                    {
                        val prefs = Preferencias()

                        if(!prefs.guardarSharedPrefsUser(this,
                                setOf(getString(R.string.keyNameUser),myUsuario.usuario),setOf(getString(R.string.keyNamePass),edtcontra.text.toString())))
                        {
                            Toast.makeText(this,"Error al guardar Preferencias",Toast.LENGTH_SHORT).show()
                        }

                        val myIntent=Intent(applicationContext,DashActivity::class.java)
                        myIntent.putExtra(keyName,myUsuario)
                        startActivity(myIntent)
                    }
                }
                else
                    Toast.makeText(this,R.string.usuario_incorrecto, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun verificarErrores():Boolean
    {
        val error=getString(R.string.campo_requerido)

        if(edtusuario.text?.isEmpty()!!)
        {
            edtusuario.error = error
            return false
        }
        else
        if(edtcontra.text?.isEmpty()!!)
        {
            edtcontra.error = error
            return false
        }

        return true
    }

    fun seguir(view: View) {
            startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(getString(R.string.face_uri))))
    }
}
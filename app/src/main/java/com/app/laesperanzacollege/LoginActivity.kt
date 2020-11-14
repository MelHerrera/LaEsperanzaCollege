package com.app.laesperanzacollege

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val keyName="USUARIO"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtusuario.setText("hamador")
        edtcontra.setText("admin2020")
        btnsesion.setOnClickListener {

            if(verificarErrores())
            {

                val usuarioDAO:UsuarioDAO?= UsuarioDAO(this)
                val myUsuario:Usuario?=usuarioDAO?.Buscar(edtusuario.text.toString(),edtcontra.text.toString())

                if(myUsuario!=null && myUsuario.id!! >0)
                {
                    val myIntent=Intent(applicationContext,DashActivity::class.java)
                    myIntent.putExtra(keyName,myUsuario)
                    startActivity(myIntent)
                }
                else
                    Toast.makeText(this,getString(R.string.usuario_incorrecto), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun verificarErrores():Boolean
    {
        val error="Campo Requerido"

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
}
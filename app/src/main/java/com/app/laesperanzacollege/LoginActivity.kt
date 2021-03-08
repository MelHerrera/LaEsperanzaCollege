package com.app.laesperanzacollege

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzaedm.model.Usuario
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var keyName=""
    private val INTERVALO = 2000 //2 segundos para salir
    private var tiempoPrimerClick: Long = 0
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
                        val userData=setOf(getString(R.string.keyNameUser),myUsuario.usuario)
                        val passData=setOf(getString(R.string.keyNamePass),edtcontra.text.toString())

                        if(!prefs.guardarSharedPrefsUser(this, userData,passData))
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

        edtusuario.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus)
                txtusuario.endIconMode=TextInputLayout.END_ICON_CLEAR_TEXT
            else
                txtusuario.endIconMode=TextInputLayout.END_ICON_NONE
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

    override fun onBackPressed() {
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            super.onBackPressed()
            this.finishAffinity()
        }else {
            Toast.makeText(this, "Presiona 2 Veces Para Salir", Toast.LENGTH_SHORT).show()
        }
        tiempoPrimerClick = System.currentTimeMillis()
    }
}
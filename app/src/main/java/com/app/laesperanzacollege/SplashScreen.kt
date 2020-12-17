package com.app.laesperanzacollege

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzaedm.model.Usuario
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        GlobalScope.launch()
        {
                val myUsuario: Usuario?=buscarUsuario(obtenerPreferencias())

                if(myUsuario!=null)
                {
                    if(myUsuario.id!! >0)
                    {
                        val myIntent=Intent(applicationContext,DashActivity::class.java)
                        myIntent.putExtra(getString(R.string.keyNameUser),myUsuario)
                        startActivity(myIntent)
                    }
                }
                else
                {
                    if(obtenerPreferenciasIntro() <= 0)
                    {
                        startActivity(Intent(applicationContext,IntroActivity::class.java))
                    }
                    else
                        startActivity(Intent(applicationContext,LoginActivity::class.java))
                }

           // delay(1000)
        }
    }

   private fun obtenerPreferencias():MutableList<String>
    {
        val mySharedPrefs= Preferencias()
        return mySharedPrefs.obtenerSharedPrefsUser(this,getString(R.string.keyNameUser),getString(R.string.keyNamePass))
    }
    private fun obtenerPreferenciasIntro():Int
    {
        val mySharedPrefs= Preferencias()
        return mySharedPrefs.obtenerSharedPrefsIntro(this,getString(R.string.keyNameIntro))
    }

    private fun buscarUsuario(myData:MutableList<String>):Usuario?
    {
        val myUsuarioDAO=UsuarioDAO(this)

        if(myData.isNotEmpty())
            {
                return myUsuarioDAO.Buscar(myData[0],myData[1])
            }
        return null
    }
}
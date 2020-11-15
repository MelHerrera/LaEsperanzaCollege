package com.app.laesperanzacollege

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.fragmentos.AdminFragment
import com.app.laesperanzacollege.fragmentos.EstudianteFragment
import com.app.laesperanzaedm.model.Usuario

class DashActivity : AppCompatActivity() {
    private var keyName=""
    private val INTERVALO = 2000 //2 segundos para salir
    private var tiempoPrimerClick: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)
        //prueba de hacer un push

        keyName=getString(R.string.keyNameUser)
        val myUsuario: Usuario?= intent.extras?.get(keyName) as Usuario

        if(myUsuario!=null)
        {
            var myFrag:Fragment?= null

            myFrag = when(myUsuario.tipoDeUsuarioId) {
                1->{
                    AdminFragment()
                }
                2->{
                    EstudianteFragment()
                }
                else->null
            }

            if (myFrag != null) {
                val myData = Bundle()
                myData.putSerializable(keyName,myUsuario)

                myFrag.arguments=myData
                supportFragmentManager.beginTransaction().replace(R.id.viewContainer,myFrag,null).commit()
            }
        }
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
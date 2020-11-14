package com.app.laesperanzacollege

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.fragmentos.AdminFragment
import com.app.laesperanzacollege.fragmentos.EstudianteFragment
import com.app.laesperanzaedm.model.Usuario

class DashActivity : AppCompatActivity() {
    private var keyName=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)

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
}
package com.app.laesperanzacollege.fragmentos

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.LoginActivity
import com.app.laesperanzacollege.QuizActivity
import com.app.laesperanzacollege.R
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.fragment_estudiante.view.*

class EstudianteFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var myView=inflater.inflate(R.layout.fragment_estudiante, container, false)

        var estudiante: Usuario?=arguments?.get("USUARIO") as Usuario

        if(estudiante!=null)
        {
            myView.txtnombres.text=estudiante.nombre+" "+estudiante.apellido
        }

        myView.btnPractica.setOnClickListener {
            startActivity(Intent(myView.context, QuizActivity::class.java))
        }
        return myView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_preferencias,menu)
        return super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.itemSalir->
            {
                var myAlert = AlertDialog.Builder(item.actionView.context)
                myAlert.setTitle("Cerrar Sesión")
                myAlert.setMessage("¿Seguro que Desea Cerrar Sesión?")
                myAlert.setNegativeButton("No", DialogInterface.OnClickListener { _, i ->
                })

                myAlert.setPositiveButton("Si", DialogInterface.OnClickListener { dialogInterface, i ->
                    startActivity(Intent(item.actionView.context, LoginActivity::class.java))
                    this.activity?.finish()
                })

                myAlert.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
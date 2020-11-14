package com.app.laesperanzacollege.fragmentos

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.ListarQuizzesActivity
import com.app.laesperanzacollege.LoginActivity
import com.app.laesperanzacollege.Preferencias
import com.app.laesperanzacollege.R
import com.app.laesperanzadao.GradoDAO
import com.app.laesperanzadao.enums.TipodeTest
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.fragment_estudiante.view.*

class EstudianteFragment : Fragment() {
    private var keyName=""
    private var myGradoDAO:GradoDAO?=null
    var myContext: Context?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView=inflater.inflate(R.layout.fragment_estudiante, container, false)
        keyName=getString(R.string.keyNameUser)
        myGradoDAO= GradoDAO(myView.context)
        myContext=container?.context

        val estudiante: Usuario?=arguments?.get(keyName) as Usuario

        if(estudiante!=null)
        {
            myView.txtnombres.text=estudiante.nombre+" "+estudiante.apellido
            myView.txtgrad.text=myGradoDAO?.Buscar(estudiante.codGrado.toString())
        }

        myView.btnPractica.setOnClickListener {
            val myIntent=Intent(myView.context, ListarQuizzesActivity::class.java)
            myIntent.putExtra(keyName,estudiante)
            myIntent.putExtra(getString(R.string.txt_tipoTest),TipodeTest.Practica)
            startActivity(myIntent)
        }

        myView.btnPrueba.setOnClickListener {
            val myIntent=Intent(myView.context, ListarQuizzesActivity::class.java)
            myIntent.putExtra(keyName,estudiante)
            myIntent.putExtra(getString(R.string.txt_tipoTest),TipodeTest.Prueba)
            startActivity(myIntent)
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
            R.id.itemCerrar->
            {
                val myAlert = AlertDialog.Builder(myContext!!)
                myAlert.setTitle(getString(R.string.text_cerrarsesion))
                myAlert.setMessage(getString(R.string.confirmar_cerrarsesion))
                myAlert.setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { _, _ ->
                })

                myAlert.setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { _, _ ->
                    var mySharedPrefs= Preferencias()
                    if(mySharedPrefs.limpiarSharedPrefs(myContext!!))
                        startActivity(Intent(myContext!!, LoginActivity::class.java))
                })

                myAlert.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
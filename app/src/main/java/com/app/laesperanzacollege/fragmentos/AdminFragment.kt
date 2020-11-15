package com.app.laesperanzacollege.fragmentos

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events.*
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.*
import com.app.laesperanzacollege.Preferencias
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.fragment_admin1.*
import kotlinx.android.synthetic.main.fragment_admin1.view.*
import java.util.*


class AdminFragment : Fragment() {
    var myContext:Context?= null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView=inflater.inflate(R.layout.fragment_admin1, container, false)

        val myUsuario: Usuario?= arguments?.get(getString(R.string.keyNameUser)) as Usuario
        myContext=container?.context


        val myToolBar=myView.myTool
        myToolBar.title=""
        myToolBar.overflowIcon=ContextCompat.getDrawable(myContext!!, R.drawable.ic_more)
        setHasOptionsMenu(true)

       (activity as AppCompatActivity?)?.setSupportActionBar(myToolBar)


        if(myUsuario!=null)
        {
            myView.txtnombres.text=myUsuario.nombre+" "+myUsuario.apellido
        }

        myView.categoria1.setOnClickListener {
            startActivity(Intent(myView.context,EstActivity::class.java))
        }
        myView.categoria2.setOnClickListener {
            startActivity(Intent(myView.context,UnidadesActivity::class.java))
        }
        myView.categoria3.setOnClickListener {
            startActivity(Intent(myView.context,QuizzesActivity::class.java))
        }

        val cal=Calendar.getInstance()

        myView.categoria4.setOnClickListener {
            startActivity(Intent(myView.context,ActividadesActivity::class.java))
        }

        myView.categoria5.setOnClickListener {
            startActivity(Intent(myView.context,FormulasActivity::class.java))
        }
        myView.categoria6.setOnClickListener {
            startActivity(Intent(myView.context,CuriosidadesActivity::class.java))
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
            myAlert.setNegativeButton(getString(R.string.no)) { _, _ ->
            }

            myAlert.setPositiveButton(android.R.string.ok) { _, _ ->
                val mySharedPrefs= Preferencias()
                if(mySharedPrefs.limpiarSharedPrefs(myContext!!))
                    startActivity(Intent(myContext!!, LoginActivity::class.java))
            }

            myAlert.show()
        }
    }
    return true
}
}
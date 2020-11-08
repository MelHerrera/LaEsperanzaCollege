package com.app.laesperanzacollege.fragmentos

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.*
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.fragment_admin1.view.*
import java.util.*

class AdminFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView=inflater.inflate(R.layout.fragment_admin1, container, false)

        val myUsuario: Usuario?= arguments?.get("USUARIO") as Usuario

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
            val dtp= DatePickerDialog(myView.context,android.R.style.ThemeOverlay_Material_Dialog_Alert, DatePickerDialog.OnDateSetListener
            { datePicker, day, month, year ->
                //The month starting from 0 and end to 11
                var fecha="$day/${month+1}/$year"
                Toast.makeText(myView.context,fecha,Toast.LENGTH_SHORT).show()

            },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))

            dtp.show()
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
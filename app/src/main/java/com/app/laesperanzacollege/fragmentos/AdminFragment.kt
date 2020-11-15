package com.app.laesperanzacollege.fragmentos

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.*
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.fragment_admin1.view.*


class AdminFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView=inflater.inflate(R.layout.fragment_admin1, container, false)

        val myUsuario: Usuario?= arguments?.get(getString(R.string.keyNameUser)) as Usuario

        if(myUsuario!=null)
        {
            myView.txtnombres.text = "${myUsuario.nombre} ${myUsuario.apellido}"
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

        myView.categoria4.setOnClickListener {
            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CONTENT_URI)
                .putExtra(TITLE,"")
                .putExtra(EVENT_LOCATION,"Aqui")
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,System.currentTimeMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,System.currentTimeMillis()+(60*60*1000))

            startActivity(intent)
        }

        myView.categoria5.setOnClickListener {
            startActivity(Intent(myView.context,FormulasActivity::class.java))
        }
        myView.categoria6.setOnClickListener {
            startActivity(Intent(myView.context,CuriosidadesActivity::class.java))
        }
        return myView
    }
}
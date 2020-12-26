package com.app.laesperanzacollege.fragmentos

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.*
import com.app.laesperanzacollege.Utils.Companion.setImage
import com.app.laesperanzacollege.Utils.Companion.toByteArray
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.fragment_admin.view.*


class AdminFragment : Fragment() {
    private var myImage:ImageView?=null
    private var myUsuario:Usuario?=null
    private var myUsuarioDAO:UsuarioDAO?=null
    private var myContext:Context?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView=inflater.inflate(R.layout.fragment_admin, container, false)

        myUsuario= arguments?.get(getString(R.string.keyNameUser)) as Usuario
        myImage=myView.imagenPerfil
        myUsuarioDAO= UsuarioDAO(myView.context)
        myContext=myView.context

        if(myUsuario!=null)
        {
            myView.txtnombres.text = "${myUsuario!!.nombre} ${myUsuario!!.apellido}"

            if(myUsuario?.imagen!=null)
            {
                setImage(myUsuario?.imagen!!, myImage)
            }
        }

        myView.capturarFoto.setOnClickListener {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),0)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && data!=null)
        {
            if(data.extras!=null)
            {
                val mbitmap=data.extras?.get("data") as Bitmap

                if(mbitmap.byteCount>0)
                {
                    setImage(mbitmap.toByteArray(), myImage)
                    actualizarImagen(mbitmap.toByteArray())
                }
            }
        }
    }
    private fun actualizarImagen(mImage:ByteArray)
    {
        if(myUsuarioDAO!=null && myUsuario!=null)
        {
            if(!myUsuarioDAO!!.actualizarImagen(mImage, myUsuario!!.id))
            {
                Toast.makeText(myContext,"Ocurrio un Error", Toast.LENGTH_LONG).show()
            }
        }
    }
}
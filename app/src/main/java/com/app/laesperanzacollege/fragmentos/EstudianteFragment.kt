package com.app.laesperanzacollege.fragmentos

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.ListarQuizzesActivity
import com.app.laesperanzacollege.LoginActivity
import com.app.laesperanzacollege.Preferencias
import com.app.laesperanzacollege.R
import com.app.laesperanzacollege.Utils.Companion.setImage
import com.app.laesperanzacollege.Utils.Companion.toByteArray
import com.app.laesperanzadao.GradoDAO
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzadao.enums.TipodeTest
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.fragment_estudiante.view.*

class EstudianteFragment : Fragment() {
    private var keyName=""
    private var myGradoDAO:GradoDAO?=null
    private var myUsuarioDAO:UsuarioDAO? = null
    private var myContext: Context?= null
    private var myImage:ImageView? = null
    private var estudiante: Usuario?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView=inflater.inflate(R.layout.fragment_estudiante, container, false)
        keyName=getString(R.string.keyNameUser)
        myGradoDAO= GradoDAO(myView.context)
        myContext=container?.context
        myImage=myView.imageView_photo
        myUsuarioDAO=UsuarioDAO(myView.context)

        estudiante=arguments?.get(keyName) as Usuario

        if(estudiante!=null)
        {
            myView.txtnombres.text= estudiante!!.nombre+" "+ estudiante!!.apellido
            myView.txtgrad.text=myGradoDAO?.Buscar(estudiante!!.codGrado.toString())

            if(estudiante?.imagen!=null)
            {
                setImage(estudiante?.imagen!!,myImage)
            }
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

        myView.photo_camera.setOnClickListener {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),0)
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
                    if(mySharedPrefs.limpiarSharedPrefs(myContext!!,Preferencias.sharedPrefsFileUser))
                        startActivity(Intent(myContext!!, LoginActivity::class.java))
                }

                myAlert.show()
            }
        }
        return super.onOptionsItemSelected(item)
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
                    setImage(mbitmap.toByteArray(),myImage)
                    actualizarImagen(mbitmap.toByteArray())
                }
            }
        }
    }

    private fun actualizarImagen(mImage:ByteArray)
    {
        if(myUsuarioDAO!=null && estudiante!=null)
        {
           if(!myUsuarioDAO!!.actualizarImagen(mImage, estudiante!!.id))
           {
               Toast.makeText(myContext,"Ocurrio un Error",Toast.LENGTH_LONG).show()
           }
        }
    }


}
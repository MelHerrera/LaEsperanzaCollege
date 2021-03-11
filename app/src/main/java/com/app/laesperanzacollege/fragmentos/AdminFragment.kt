package com.app.laesperanzacollege.fragmentos

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.*
import com.app.laesperanzacollege.Utils.Companion.setImage
import com.app.laesperanzacollege.Utils.Companion.toByteArray
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzaedm.model.Usuario
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.activity_agregar_quiz.*
import kotlinx.android.synthetic.main.activity_agregar_quiz.app_bar
import kotlinx.android.synthetic.main.activity_agregar_quiz.toolbar_layout
import kotlinx.android.synthetic.main.fragment_admin.*
import kotlinx.android.synthetic.main.fragment_admin.view.*
import kotlin.math.abs


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

        setHasOptionsMenu(true)

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
            startActivity(Intent(myView.context,CuriosidadesActivity::class.java))
        }

        myView.categoria5.setOnClickListener {
            startActivity(Intent(myView.context,ActividadesActivity::class.java))
        }
        myView.categoria6.setOnClickListener {
            startActivity(Intent(myView.context,FormulasActivity::class.java))
        }
        return myView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_preferencias,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.item_edit_perfil).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.itemCerrar->
            {

                val myAlert = AlertDialog.Builder(myContext!!)
                myAlert.setTitle(getString(R.string.text_cerrarsesion))
                myAlert.setMessage(getString(R.string.confirmar_cerrarsesion))
                myAlert.setNegativeButton(getString(R.string.negativa)) { _, _ ->
                }

                myAlert.setPositiveButton(getString(R.string.afirmativa)) { _, _ ->
                    val mySharedPrefs= Preferencias()
                    if(mySharedPrefs.limpiarSharedPrefs(myContext!!,Preferencias.sharedPrefsFileUser))
                        startActivity(Intent(myContext, LoginActivity::class.java))
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
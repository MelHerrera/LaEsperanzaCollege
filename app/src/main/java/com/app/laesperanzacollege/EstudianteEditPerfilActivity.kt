package com.app.laesperanzacollege

import Observers.EstudianteObserver
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.app.laesperanzacollege.Utils.Companion.toByteArray
import com.app.laesperanzadao.GradoDAO
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.activity_estudiante_edit_perfil.*
import kotlinx.android.synthetic.main.activity_estudiante_edit_perfil.txtnombres

class EstudianteEditPerfilActivity : AppCompatActivity() {
    private var estudiante:Usuario=Usuario()
    private var foto:ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudiante_edit_perfil)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear)

        estudiante=intent?.extras?.get("USUARIO") as Usuario
        foto=imageView_photo

        txtnombres.text= estudiante!!.nombre+" "+ estudiante.apellido
        txtgrad.text=GradoDAO(this).Buscar(estudiante.codGrado.toString())
        editUsuario.setText(estudiante.usuario)
        editContra.setText(estudiante.contrase)

        if(estudiante.imagen!=null)
        {
            Utils.setImage(estudiante.imagen!!, foto)
        }

        photo_camera.setOnClickListener {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),0)
        }

        btnGuardar.setOnClickListener {
            if(validar())
            {
                var passData:Set<String>?= setOf()
                val prefs = Preferencias()

                if(editContra.text.toString().isEmpty())
                    UsuarioDAO(this).actualizar(editUsuario.text.toString(),null,estudiante.id!!)
                else
                {
                    UsuarioDAO(this).actualizar(editUsuario.text.toString(),editContra.text.toString(),estudiante.id!!)
                    passData=setOf(getString(R.string.keyNamePass),editContra.text.toString())
                    mEstuObserver?.updatePass(editContra.text.toString())
                }

                mEstuObserver?.updateUser(editUsuario.text.toString())

                //Update sharedPreferences File
                val userData=setOf(getString(R.string.keyNameUser),editUsuario.text.toString())

                if(!prefs.guardarSharedPrefsUser(this, userData,passData))
                {
                    Toast.makeText(this,"Error al guardar Preferencias",Toast.LENGTH_SHORT).show()
                }

                this.finish()
            }
        }
    }

    private fun validar(): Boolean {
        if(editUsuario.text.toString().isEmpty())
        {
            editUsuario.error="Campo Vacio"
            return false
        }
        else
            editUsuario.error=null
        return true
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
                    Utils.setImage(mbitmap.toByteArray(), foto)
                    actualizarImagen(mbitmap.toByteArray())
                }
            }
        }
    }

    private fun actualizarImagen(mImage:ByteArray)
    {
        if(!UsuarioDAO(this).actualizarImagen(mImage, estudiante.id))
        {
            Toast.makeText(this,"Ocurrio un Error", Toast.LENGTH_LONG).show()
        }
        else
            mEstuObserver?.updateImage(mImage)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object{
        var mEstuObserver:EstudianteObserver?=null
    }
}
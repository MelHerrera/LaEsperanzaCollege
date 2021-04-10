package com.app.laesperanzacollege

import Observers.UsuarioObserver
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.laesperanzacollege.Utils.Companion.crearCustomSnackbar
import com.app.laesperanzadao.GradoDAO
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Grado
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.activity_agregar_estu.*

class AgregarEstuActivity : AppCompatActivity() {
    private var myGradoDAO:GradoDAO?=null
    private var grados= arrayListOf<String>()
    private var listGrados:ArrayList<Grado>?=null
    private var myOperacion:String?=null
    private var myUsuario:Usuario?=null
    private var mySeccion:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_estu)

        myGradoDAO=GradoDAO(this)
        listGrados=myGradoDAO!!.listarGrados()
        myOperacion=intent.extras?.get("OPERACION").toString()
        val extras=intent.extras?.get("USUARIO")

        if (myGradoDAO != null) {

            listGrados?.forEach {
                if(it.codGrado!="0grd")
                    grados.add(it.descripcion.toString())
            }

            spGrados.adapter=ArrayAdapter(this,R.layout.list_item_1,grados)
        }

        if(extras!=null)
            myUsuario=extras as Usuario

        if(myOperacion==OperacionesCrud.Editar.toString())
        {
            setControles(myUsuario!!)
        }

        btnCancelar.setOnClickListener {
            this.finish()
        }

        chipSec.setOnCheckedChangeListener { _, checkedId ->
            mySeccion = when(checkedId) {
                R.id.txtSecA->{
                    txtSecA.text.toString()
                }
                R.id.txtSecB->{
                    txtSecB.text.toString()
                }
                R.id.txtSecC->{
                    txtSecC.text.toString()
                }
                else-> null
            }
        }

        btnGuardar.setOnClickListener {
            if(validar())
            {
                val myUsuarioDAO=UsuarioDAO(this)

                myUsuario=asignar(myUsuario)

                if(myOperacion==OperacionesCrud.Agregar.toString())
                {
                    if(myUsuarioDAO.insertar(myUsuario!!))
                    {
                        val nuevoUsuario=myUsuarioDAO.Buscar(myUsuario!!.usuario.toString(), myUsuario!!.contrase.toString())

                        if(nuevoUsuario!=null)
                            myUsuarioObserver?.agregarUsuario(nuevoUsuario)

                        limpiar()
                    }
                    else
                        Toast.makeText(this,"Lo siento!!! No pude Guardar la Información",Toast.LENGTH_LONG).show()
                }
                else
                {
                   if(myUsuarioDAO.actualizar(myUsuario!!))
                   {
                       myUsuarioObserver?.actualizarUsuario(myUsuario!!)
                       this.finish()
                   }
                }


            }
        }
    }

    private fun validar():Boolean
    {
        if(edtNombres.text?.isEmpty()!!)
        {
            edtNombres.error="Campo Requerido"
            txtnombres.setHelperTextColor(ColorStateList.valueOf(Color.RED))
            return false
        }
        else
            txtnombres.helperText=null

        if(edtApellidos.text?.isEmpty()!!)
        {
            edtApellidos.error="Campo Requerido"
            txtApellidos.setHelperTextColor(ColorStateList.valueOf(Color.RED))
            return false
        }
        else
            txtApellidos.helperText=null

        if(edtUsuario.text?.isEmpty()!!)
        {
            edtUsuario.error="Campo Requerido"
            txtUsuario.setHelperTextColor(ColorStateList.valueOf(Color.RED))
            return false
        }
        else
            txtUsuario.helperText=null

        if(mySeccion==null)
        {
            val mSnack=crearCustomSnackbar(viewPrincipal,Color.RED,android.R.drawable.stat_notify_error,
                getString(R.string.error_incomplete_data,getString(R.string.seccion)),layoutInflater)
            mSnack.show()
            return false
        }

        return true
    }

    private fun asignar(myUsu:Usuario?):Usuario
    {
        var myUsuaT=myUsu

        if(myUsuaT==null)
        {
            myUsuaT= Usuario()
        }

        myUsuaT.nombre= edtNombres.text?.trim().toString()
        myUsuaT.apellido=edtApellidos.text?.trim().toString()
        myUsuaT.codGrado=listGrados!![spGrados.selectedItemPosition+1].codGrado
        myUsuaT.tipoDeUsuarioId=2
        myUsuaT.usuario=edtUsuario.text?.trim().toString()
        myUsuaT.contrase=edtContra.text?.trim().toString()
        myUsuaT.seccion=mySeccion

        return myUsuaT
    }
    private fun setControles(myUsu: Usuario)
    {
        edtNombres.setText(myUsu.nombre)
        edtApellidos.setText(myUsu.apellido)

        val gradoSelected=listGrados?.find{ x->x.codGrado==myUsu.codGrado }
        val posSelected:Int?= listGrados?.indexOf(gradoSelected)

            if(posSelected!=null)
            {
                spGrados.setSelection(posSelected-1)
            }

        mySeccion=myUsu.seccion

        when(myUsu.seccion)
        {
            "A"-> txtSecA.isChecked=true
            "B"-> txtSecB.isChecked=true
            "C"-> txtSecC.isChecked=true
            else-> mySeccion=null
        }

        edtUsuario.setText(myUsu.usuario)
    }

    private fun limpiar()
    {
        edtNombres.text?.clear()
        edtApellidos.text?.clear()
        edtUsuario.text?.clear()
        edtContra.text?.clear()
        chipSec.clearCheck()
    }

    companion object
    {
        var myUsuarioObserver: UsuarioObserver?=null
    }
}
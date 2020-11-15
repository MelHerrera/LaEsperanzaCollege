package com.app.laesperanzacollege

import Observers.UsuarioObserver
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.ArrayAdapter
import android.widget.Toast
import com.app.laesperanzadao.GradoDAO
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Grado
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.activity_agregar_estu.*

class AgregarEstuActivity : AppCompatActivity() {
    var myGradoDAO:GradoDAO?=null
    var grados= arrayListOf<String>()
    var listGrados:ArrayList<Grado>?=null
    var myOperacion:String?=null
    var myUsuario:Usuario?=null
    var mySeccion:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_estu)

        myGradoDAO=GradoDAO(this)
        listGrados=myGradoDAO!!.listarGrados()
        myOperacion=intent.extras?.get("OPERACION").toString()
        val extras=intent.extras?.get("USUARIO")

        if (myGradoDAO != null) {
            for (item in listGrados!!)
            {
                if(item.codGrado!="0grd")
                {
                    grados.add(item.descripcion.toString())
                }
            }
            spGrados.adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,grados)
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

        chipSec.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId)
            {
                R.id.txtSecA->{mySeccion=txtSecA.text.toString()}
                R.id.txtSecB->{mySeccion=txtSecB.text.toString()}
                R.id.txtSecC->{mySeccion=txtSecC.text.toString()}
                else->{mySeccion=null}
            }
        }

        btnGuardar.setOnClickListener {
            if(Validar())
            {
                val myUsuarioDAO=UsuarioDAO(this)

                myUsuario=Asignar(myUsuario)

                if(myOperacion==OperacionesCrud.Agregar.toString())
                {
                    if(myUsuarioDAO.insertar(myUsuario!!))
                    {
                        val nuevoUsuario=myUsuarioDAO.Buscar(myUsuario!!.usuario.toString(), myUsuario!!.contrase.toString())

                        if(nuevoUsuario!=null)
                        {
                            myUsuario!!.id=nuevoUsuario.id
                        }
                        myUsuarioObserver?.usuarioSaved(myUsuario!!)
                        Limpiar()
                    }
                    else
                        Toast.makeText(this,"Lo siento!!! No pude Guardar la Información",Toast.LENGTH_LONG).show()
                }
                else
                {
                   if(myUsuarioDAO.actualizar(myUsuario!!))
                   {
                       myUsuarioObserver?.usuarioSaved(myUsuario!!)
                       this.finish()
                   }
                }


            }
        }
    }

    fun Validar():Boolean
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

        if(edtContra.text?.isEmpty()!!)
        {
            edtContra.error="Campo Requerido"
            txtContra.setHelperTextColor(ColorStateList.valueOf(Color.RED))
            return false
        }
        else
            txtContra.helperText=null

        if(mySeccion==null)
        {
            Toast.makeText(this,getString(R.string.error_seccion),Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    fun Asignar(myUsu:Usuario?):Usuario
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
    fun setControles(myUsu: Usuario)
    {
        edtNombres.setText(myUsu.nombre)
        edtApellidos.setText(myUsu.apellido)

        val gradoSelected=listGrados?.find{ x->x.codGrado==myUsu.codGrado }
        val posSelected:Int?= listGrados?.indexOf(gradoSelected)

            if(posSelected!=null)
            {
                spGrados.setSelection(posSelected-1)
            }

        edtUsuario.setText(myUsu.usuario)
        edtContra.setText("********")
        edtContra.transformationMethod = PasswordTransformationMethod.getInstance()
        edtContra.isEnabled=false

    }

    fun Limpiar()
    {
        edtNombres.text?.clear()
        edtApellidos.text?.clear()
        edtUsuario.text?.clear()
        edtContra.text?.clear()
        chipSec.clearCheck()
    }

    companion object
    {
        var myUsuarioObserver:UsuarioObserver?=null
    }
}
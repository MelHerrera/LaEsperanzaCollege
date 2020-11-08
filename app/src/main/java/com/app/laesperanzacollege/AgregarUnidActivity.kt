package com.app.laesperanzacollege

import Observers.UnidadObserver
import android.graphics.Color
import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.core.widget.addTextChangedListener
import com.app.laesperanzadao.GradoDAO
import com.app.laesperanzadao.UnidadDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Grado
import com.app.laesperanzaedm.model.Unidad
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.activity_agregar__unidad.*
import kotlinx.android.synthetic.main.activity_agregar_estu.*

class AgregarUnidActivity : AppCompatActivity() {
    var myGradoDAO: GradoDAO?=null
    var grados= arrayListOf<String>()
    var listGrados:ArrayList<Grado>?=null
    var myOperacion:String?=null
    var myUnidad:Unidad?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar__unidad)

        myOperacion=intent.extras?.get("OPERACION").toString()
        var extras=intent.extras?.get("UNIDAD")


        myGradoDAO=GradoDAO(this)
        listGrados=myGradoDAO!!.listarGrados()

        if (myGradoDAO != null) {
            for (item in listGrados!!)
            {
                if(item.codGrado!="0grd")
                {
                    grados.add(item.descripcion.toString())
                }
            }
            spGrads.adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,grados)
        }

        if(extras!=null)
            myUnidad=extras as Unidad

        if(myOperacion==OperacionesCrud.Editar.toString())
        {
            setControles(myUnidad!!)
        }

        edtNumUnidad.addTextChangedListener(object:TextWatcher
        {
            override fun afterTextChanged(p0: Editable?) {
              NumUnidad.text=p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(edtNumUnidad.length()>txtNumUnidad.counterMaxLength)
                {
                    txtNumUnidad.error="Numero de Unidad no Permitida"
                }
                else
                {
                    txtNumUnidad.isErrorEnabled=false
                }
            }

        })


        edtDes.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                txtDesc.text=p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        btnSalir.setOnClickListener {
            this.finish()
        }

        btnGuard.setOnClickListener {
            if(validar())
            {
                var myUnidadDAO=UnidadDAO(this)
                myUnidad=Asignar(myUnidad)

                if(myOperacion==OperacionesCrud.Agregar.toString())
                {
                    if(myUnidadDAO.Insertar(myUnidad!!))
                    {
                        myUnidadObserver?.unidadSaved(myUnidad!!)
                        edtDes.setText("")
                        txtDesc.text = "Texto Predeterminado"
                        edtNumUnidad.setText("")
                        NumUnidad.text="0"

                        Toast.makeText(this,"Se Guardo con Exito",Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(this,"Nombre o Numero de Unidad ya Existen",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    if(myUnidadDAO.actualizar(myUnidad!!))
                    {
                        myUnidadObserver?.unidadSaved(myUnidad!!)
                        this.finish()
                    }
                    else
                    {
                        Toast.makeText(this,"No se pudo Actualizar",Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

    fun setControles(myUnidad: Unidad)
    {
        edtNumUnidad.setText(myUnidad.numUnidad.toString())
        edtNumUnidad.isEnabled=false

        edtDes.setText(myUnidad.descripcion.toString())
        NumUnidad.text=myUnidad.numUnidad.toString()
        txtDesc.text=myUnidad.descripcion

        var gradoSelected=listGrados?.find{ x->x.codGrado==myUnidad.codGrado }
        var posSelected:Int?= listGrados?.indexOf(gradoSelected)

        if(posSelected!=null)
        {
            spGrads.setSelection(posSelected-1)
        }
    }

    fun Asignar(myUnidad:Unidad?):Unidad
    {
        var myUnidT=myUnidad

        if(myUnidT==null)
        {
            myUnidT= Unidad()
        }

        myUnidT.numUnidad=Integer.parseInt(edtNumUnidad.text.toString())
        myUnidT.descripcion=edtDes.text?.trim().toString()
        myUnidT.codGrado=listGrados!![spGrads.selectedItemPosition+1].codGrado

        return myUnidT
    }

    fun validar():Boolean
    {
        if(edtNumUnidad.text.toString().isEmpty())
        {
            txtNumUnidad.error="No debe quedar Vacio"
            return false
        }
        else
            txtNumUnidad.error=null

        if(edtNumUnidad.text.toString()!! == "0")
        {
            txtNumUnidad.error="Debe ser mayor a 0"
            return false
        }
        else
            txtNumUnidad.error=null

        if(edtDes.text.toString().isEmpty())
        {
            txtDescrip.error="No debe quedar Vacio"
            return false
        }
        else
            txtDescrip.error=null

        return true
    }

    companion object
    {
        var myUnidadObserver:UnidadObserver?=null
    }
}
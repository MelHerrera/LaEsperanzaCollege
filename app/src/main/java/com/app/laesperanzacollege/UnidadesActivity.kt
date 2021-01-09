package com.app.laesperanzacollege

import Observers.UnidadObserver
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.UnidAdapter
import com.app.laesperanzadao.UnidadDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Unidad
import com.app.laesperanzacollege.Utils.Companion.spanCalc
import kotlinx.android.synthetic.main.activity_unidades.*

class UnidadesActivity : AppCompatActivity(),UnidadObserver {
    private var myUnidadesAdapter:UnidAdapter?=null
    var myLayoutManager:RecyclerView.LayoutManager?=null
    private var myListUnidad:ArrayList<Unidad> =arrayListOf()
    private var myUnidadDAO:UnidadDAO?=null
    private var selectedItems:ArrayList<Unidad> =arrayListOf()
    private var myTool:Toolbar?=null
    private var myTxtCantidad:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidades)

        myTool=myToolb
        myTool?.title=getString(R.string.txt_unidades)
        myTool?.setTitleTextColor(Color.WHITE)
        myTool?.navigationIcon=ResourcesCompat.getDrawable(resources,R.drawable.ic_backspace,null)
        setSupportActionBar(myTool)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        floatingAgregarUnidad.setOnClickListener {
            AgregarUnidActivity.myUnidadObserver=this
            val myIntent=Intent(this,AgregarUnidActivity::class.java)
            myIntent.putExtra("OPERACION",OperacionesCrud.Agregar.toString())
            startActivity(myIntent)
        }

        myUnidadDAO=UnidadDAO(this)
        myListUnidad= myUnidadDAO!!.listarUnidades()

        myTxtCantidad=txtCantidadUnidades

        if(myTxtCantidad!=null)  Validador.validarCantidad(myTxtCantidad!!,myListUnidad)

        UnidAdapter.myUnidadObserver=this
        myUnidadesAdapter= UnidAdapter(myListUnidad)

        if(recyUnidades!=null)
        {
            recyUnidades.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        recyUnidades.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        myLayoutManager=GridLayoutManager(this@UnidadesActivity,spanCalc(recyUnidades,this@UnidadesActivity))
                        recyUnidades.layoutManager=myLayoutManager
                    }
                })
        }
        else
            myLayoutManager=GridLayoutManager(this,2)

        recyUnidades.layoutManager=myLayoutManager
        recyUnidades.adapter=myUnidadesAdapter
    }

    override fun startSelection(pos: Int,selected:Boolean)
    {

     if(selected)
        {
            addSelectedItem(myListUnidad[pos])
        }
        else
     {
         removeSelectedItem(myListUnidad[pos])
     }

        limpiarMenu()
    }

    override fun unidadSaved(myUnidad: Unidad) {

        selectedItems=arrayListOf()
        limpiarMenu()

        val myUnid=myListUnidad.find { x->x.numUnidad==myUnidad.numUnidad}

        if(myUnid!=null)
        {
            myUnid.codGrado=myUnidad.codGrado
            myUnid.descripcion=myUnidad.descripcion
            myUnid.numUnidad=myUnidad.numUnidad
        }
        else
            myListUnidad.add(myUnidad)

        myUnidadesAdapter?.notifyDataSetChanged()
        if(myTxtCantidad!=null)  Validador.validarCantidad(myTxtCantidad!!,myListUnidad)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.myItemDelete->
            {
                val myAlert=AlertDialog.Builder(this)

                myAlert.setTitle("Confirmar")
                myAlert.setMessage("¿Seguro que Desea Eliminar?")
                myAlert.setPositiveButton("Si") { _, _ ->

                    val unidToDelete:ArrayList<String> = arrayListOf()

                    for (it in selectedItems) {
                        unidToDelete.add(it.numUnidad.toString())
                    }

                    val res=myUnidadDAO?.EliminarUnidades(unidToDelete)
                    print(res)

                    myListUnidad.removeAll(selectedItems)
                    selectedItems= arrayListOf()
                    limpiarMenu()
                    myUnidadesAdapter?.notifyDataSetChanged()

                    if(myTxtCantidad!=null)  Validador.validarCantidad(myTxtCantidad!!,myListUnidad)

                }
                myAlert.setNegativeButton("No") { _, _ ->  }
                myAlert.show()
            }

            R.id.itemEdit->
            {
                val myIntent=Intent(this,AgregarUnidActivity::class.java)
                myIntent.putExtra("UNIDAD",selectedItems[0])
                myIntent.putExtra("OPERACION",OperacionesCrud.Editar.toString())
                AgregarUnidActivity.myUnidadObserver=this
                startActivity(myIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun limpiarMenu()
    {
        if(selectedItems.size==0)
        {
            myTool?.menu?.clear()
            //first=0
            selectedItems=arrayListOf()
        }

        if(selectedItems.size==1)
        {
            if(myTool?.menu?.hasVisibleItems()==false)
            myTool?.inflateMenu(R.menu.menu_unidades)

            myTool?.inflateMenu(R.menu.menu_unidades_edit)
        }

        if(selectedItems.size>1)
        {
            myTool?.menu?.clear()
            myTool?.inflateMenu(R.menu.menu_unidades)
        }

    }

    private fun addSelectedItem(myUnidad: Unidad)
    {
        selectedItems.add(myUnidad)
    }

    private fun removeSelectedItem(myUnidad: Unidad)
    {
        selectedItems.remove(myUnidad)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
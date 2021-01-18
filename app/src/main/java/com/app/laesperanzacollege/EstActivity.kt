package com.app.laesperanzacollege

import Observers.UsuarioObserver
import Observers.UsuarioObserverMain
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.EstuAdapter
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.activity_est.*
import java.util.*
import kotlin.collections.ArrayList

class EstActivity : AppCompatActivity(), UsuarioObserverMain,UsuarioObserver {
    private var myLayoutManager: RecyclerView.LayoutManager?=null
    var myAdapter:EstuAdapter?=null
    private var myUsuario:UsuarioDAO?=null
    private var listEstu:ArrayList<Usuario> = arrayListOf()
    private var cantAlumnos:LinearLayoutCompat?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_est)

        myUsuario=UsuarioDAO(this)
        cantAlumnos=viewCantAlumnos

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listEstu = myUsuario!!.listarEstudiantes()

        //asignar dinamicamente el texto que tendra el textview cuando no hayan datos
        txtCantAlumnos.text=getString(R.string.sin_datos,getString(R.string.txt_estudiantes))

        if(cantAlumnos!=null) Validador.validarCantidad(cantAlumnos!!,listEstu)


        myLayoutManager=LinearLayoutManager(this)

        EstuAdapter.myUsuarioObserverMain=this
        EstuAdapter.myUsuarioObserver=this
        myAdapter= EstuAdapter(listEstu)

        recyEstudiantes.layoutManager=myLayoutManager
        recyEstudiantes.adapter=myAdapter

        floatingAgregar.setOnClickListener {
            val myNewActivity=Intent(this,AgregarEstuActivity::class.java)
            myNewActivity.putExtra("OPERACION",OperacionesCrud.Agregar)
            AgregarEstuActivity.myUsuarioObserver=this
            startActivity(myNewActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_estudiante,menu)
        val search = menu?.findItem(R.id.app_bar_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = getString(R.string.txtsearch)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                myAdapter?.filter?.filter(newText.toString().toUpperCase(Locale.ROOT))
                return true
            }
        })

        return true
    }

    override fun usuarioRemoved(posicion: Int) {
        listEstu.removeAt(posicion)
        myAdapter?.notifyDataSetChanged()
        if(cantAlumnos!=null) Validador.validarCantidad(cantAlumnos!!,listEstu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun actualizarUsuario(mUsuario: Usuario) {
       val indexUsu=listEstu.indexOfFirst { it.id==mUsuario.id }

        if(indexUsu!=-1)
            listEstu[indexUsu]=mUsuario

        myAdapter?.notifyDataSetChanged()
       if(cantAlumnos!=null) Validador.validarCantidad(cantAlumnos!!,listEstu)
    }

    override fun agregarUsuario(mUsuario: Usuario) {
        listEstu.add(mUsuario)

        myAdapter?.notifyDataSetChanged()
        if(cantAlumnos!=null) Validador.validarCantidad(cantAlumnos!!,listEstu)
    }

}
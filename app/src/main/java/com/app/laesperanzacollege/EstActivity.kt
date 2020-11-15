package com.app.laesperanzacollege

import Observers.UsuarioObserver
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.EstuAdapter
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.activity_est.*

class EstActivity : AppCompatActivity(),UsuarioObserver {
    var myLayoutManager: RecyclerView.LayoutManager?=null
    var myAdapter:EstuAdapter?=null
    var myUsuario:UsuarioDAO?=null
    var listEstu:ArrayList<Usuario> = arrayListOf()
    var cantAlumnos:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_est)

        myUsuario=UsuarioDAO(this)
        cantAlumnos=txtCantAlumnos

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        listEstu = myUsuario!!.listarEstudiantes()
        if(cantAlumnos!=null) Validador.validarCantidad(cantAlumnos!!,listEstu)


        myLayoutManager=LinearLayoutManager(this)

        EstuAdapter.myUsuarioObserver=this
        myAdapter= EstuAdapter(listEstu)

        recyEstudiantes.layoutManager=myLayoutManager
        recyEstudiantes.adapter=myAdapter

        floatingAgregar.setOnClickListener {
            var myNewActivity=Intent(this,AgregarEstuActivity::class.java)
            myNewActivity.putExtra("OPERACION",OperacionesCrud.Agregar)
            AgregarEstuActivity.myUsuarioObserver=this
            startActivity(myNewActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_estudiante,menu)
        val search = menu?.findItem(R.id.app_bar_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Buscar"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                myAdapter?.filter?.filter(newText.toString().toUpperCase())

                return true
            }
        })

        return true
    }

    override fun usuarioSaved(myUsuario: Usuario)
    {
        var existe=false

        for (item in listEstu)
        {
            if(item.id==myUsuario.id)
            {
                item.nombre=myUsuario.nombre
                item.usuario=myUsuario.nombre
                item.codGrado=myUsuario.codGrado
                item.apellido=myUsuario.apellido
                item.usuario=myUsuario.usuario
                existe=true
            }
        }

        if(!existe)
            listEstu.add(myUsuario)

        myAdapter?.notifyDataSetChanged()
        if(cantAlumnos!=null) Validador.validarCantidad(cantAlumnos!!,listEstu)
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

}
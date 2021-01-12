package com.app.laesperanzacollege.adaptadores

import Observers.UsuarioObserver
import Observers.UsuarioObserverMain
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.AgregarEstuActivity
import com.app.laesperanzacollege.R
import com.app.laesperanzadao.GradoDAO
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.item_estu.view.*
import java.util.*
import kotlin.collections.ArrayList

class EstuAdapter(val listEstudiantes:ArrayList<Usuario>):
    RecyclerView.Adapter<EstuAdapter.MyViewHolder>(), Filterable {
    var myEstFilterList= arrayListOf<Usuario>()
    init {
        myEstFilterList.addAll(listEstudiantes)
    }
    companion object
    {
        var myUsuarioObserverMain: UsuarioObserverMain?=null
        var myUsuarioObserver:UsuarioObserver?=null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView=LayoutInflater.from(parent.context).inflate(R.layout.item_estu,parent,false)
        return MyViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return listEstudiantes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      holder.bindholder(listEstudiantes[position])
    }

   class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),UsuarioObserver {

        fun bindholder(myEstu:Usuario)
        {
            val nombres=itemView.nombres
            val grado=itemView.grad
            val btnBorrar=itemView.imgbtnEliminar
            val btnEditar=itemView.imgBtnEditar
            val img=itemView.imgPer


            val myGrado= GradoDAO(itemView.context)
            val myUsuDAO=UsuarioDAO(itemView.context)

            val gra=myGrado.Buscar(myEstu.codGrado.toString())
            img.setBackgroundResource(R.drawable.profile)

            nombres.text="${myEstu.nombre} ${myEstu.apellido}"
            grado.text=gra

            btnBorrar.setOnClickListener {
                val myAlert=AlertDialog.Builder(itemView.context)

                myAlert.setMessage("¿Estás Seguro que Deseas Eliminar?")
                myAlert.setTitle("Eliminar")
                myAlert.setIcon(android.R.drawable.ic_menu_delete)

                myAlert.setNegativeButton("No") { _, _ ->
                    myAlert.create().dismiss()
                }

                myAlert.setPositiveButton("Si") { _, _ ->
                    if(myUsuDAO.Eliminar(myEstu.id)) {
                        myUsuarioObserverMain?.usuarioRemoved(adapterPosition)
                    } else
                        Toast.makeText(itemView.context,"No se pudo Eliminar",Toast.LENGTH_SHORT).show()
                }

                myAlert.show()

            }

            btnEditar.setOnClickListener {

                val myNewActivity=Intent(itemView.context,AgregarEstuActivity::class.java)
                myNewActivity.putExtra("OPERACION", OperacionesCrud.Editar)
                myNewActivity.putExtra("USUARIO",myEstu)
                AgregarEstuActivity.myUsuarioObserver=this
                itemView.context.startActivity(myNewActivity)
            }
        }

       override fun actualizarUsuario(mUsuario: Usuario) {
           myUsuarioObserver?.actualizarUsuario(mUsuario)
       }

       override fun agregarUsuario(mUsuario: Usuario) {
          //nothing to do
       }

   }

    override fun getFilter(): Filter {
        return object : Filter()
        {
            override fun performFiltering(wordToSearch: CharSequence?): FilterResults {
                val myFilter= FilterResults()

                if(wordToSearch?.length==0)
                {
                    myFilter.values=myEstFilterList
                }
                else
                {
                    myFilter.values = listEstudiantes.filter { x->x.nombre?.toUpperCase(Locale.ROOT)!!.contains(wordToSearch.toString()) }
                }
                return myFilter
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                listEstudiantes.clear()
                listEstudiantes.addAll(results?.values as ArrayList<Usuario>)
                notifyDataSetChanged()
            }

        }
    }
}
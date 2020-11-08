package com.app.laesperanzacollege.adaptadores

import Observers.UsuarioObserver
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.AgregarEstuActivity
import com.app.laesperanzacollege.R
import com.app.laesperanzadao.GradoDAO
import com.app.laesperanzadao.UsuarioDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Usuario
import kotlinx.android.synthetic.main.item_estu.view.*
import java.util.zip.Inflater

class EstuAdapter(val listEstudiantes:ArrayList<Usuario>):
    RecyclerView.Adapter<EstuAdapter.MyViewHolder>() {

    companion object
    {
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

   class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), UsuarioObserver {

        fun bindholder(myEstu:Usuario)
        {
            var nombres=itemView.nombres
            var grado=itemView.grad
            var btnBorrar=itemView.imgbtnEliminar
            var btnEditar=itemView.imgBtnEditar
            var img=itemView.imgPer


            var myGrado:GradoDAO= GradoDAO(itemView.context)
            var myUsuDAO=UsuarioDAO(itemView.context)

            var gra=myGrado.Buscar(myEstu.codGrado.toString())
            img.setBackgroundResource(R.drawable.profile)

            nombres.text=myEstu.nombre+" "+myEstu.apellido
            grado.text=gra

            btnBorrar.setOnClickListener {
                var myAlert=AlertDialog.Builder(itemView.context)

                myAlert.setMessage("¿Estás Seguro que Deseas Eliminar?")

                myAlert.setNegativeButton("No") { _, _ ->
                }

                myAlert.setPositiveButton("Si",DialogInterface.OnClickListener { _, i ->
                    if(myUsuDAO.Eliminar(myEstu.id))
                    {
                        var posicion:Int=adapterPosition
                        myUsuarioObserver?.usuarioRemoved(posicion)
                    }
                    else
                       Toast.makeText(itemView.context,"No se pudo Eliminar",Toast.LENGTH_SHORT).show()
                })

                myAlert.show()

            }

            btnEditar.setOnClickListener {

                var myNewActivity=Intent(itemView.context,AgregarEstuActivity::class.java)
                myNewActivity.putExtra("OPERACION", OperacionesCrud.Editar)
                myNewActivity.putExtra("USUARIO",myEstu)
                AgregarEstuActivity.myUsuarioObserver=this
                itemView.context.startActivity(myNewActivity)
            }
        }

       override fun usuarioSaved(myUsuario: Usuario) {
           myUsuarioObserver?.usuarioSaved((myUsuario))
       }

       override fun usuarioRemoved(posicion: Int) {
          //nothing
       }
   }
}
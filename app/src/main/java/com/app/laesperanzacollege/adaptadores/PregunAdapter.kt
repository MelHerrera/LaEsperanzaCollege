package com.app.laesperanzacollege.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.R
import com.app.laesperanzaedm.model.Pregunta
import kotlinx.android.synthetic.main.item_list_quizzpregunta.view.*

class PregunAdapter(val myListPreguntas:ArrayList<Pregunta>): RecyclerView.Adapter<PregunAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView= LayoutInflater.from(parent.context).inflate(R.layout.item_list_quizzpregunta,parent,false)
        return MyViewHolder(myView)
    }

    override fun getItemCount(): Int {
       return myListPreguntas.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindHolder(myListPreguntas[position])
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var descripcion=itemView.txtPreg

        fun bindHolder(pregunta: Pregunta)
        {
            descripcion.text=pregunta.descripcion
        }
    }
}
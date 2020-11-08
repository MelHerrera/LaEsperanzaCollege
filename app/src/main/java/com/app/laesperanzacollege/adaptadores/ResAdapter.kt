package com.app.laesperanzacollege.adaptadores

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.R
import com.app.laesperanzaedm.model.Respuesta
import kotlinx.android.synthetic.main.item_list_preguntaresp.view.*

class ResAdapter(var myListRespuesta:ArrayList<Respuesta>):RecyclerView.Adapter<ResAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView= LayoutInflater.from(parent.context).inflate(R.layout.item_list_preguntaresp,parent,false)
        return MyViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return myListRespuesta.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindHolder(myListRespuesta[position])
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var descripcion=itemView.textResp

        init {
            descripcion.setTextColor(Color.BLACK)
        }

        fun bindHolder(respuesta: Respuesta)
        {
            descripcion.text=respuesta.descripcion
        }
    }
}
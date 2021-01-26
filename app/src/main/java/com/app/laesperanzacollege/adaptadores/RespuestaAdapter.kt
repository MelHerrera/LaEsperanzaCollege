package com.app.laesperanzacollege.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.AgregarRespuestaActivity
import com.app.laesperanzacollege.R
import com.app.laesperanzacollege.interfaces.NotifyUpdateRecyclerView
import com.app.laesperanzaedm.model.Respuesta
import kotlinx.android.synthetic.main.item_respuesta.view.*

class RespuestaAdapter(var myListRespuesta:ArrayList<Respuesta>, var NotifyUpdateRecy: NotifyUpdateRecyclerView):RecyclerView.Adapter<RespuestaAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView= LayoutInflater.from(parent.context).inflate(R.layout.item_respuesta,parent,false)
        return MyViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return myListRespuesta.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindHolder(myListRespuesta[position],myListRespuesta,NotifyUpdateRecy)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var descripcion: TextView =itemView.textRespuesta
        private var counter: TextView =itemView.imgIconResCounter
        var iconCorecto: ImageView =itemView.iconCorrecto
        var iconQuitar:ImageView = itemView.imgDelete

        fun bindHolder(respuesta: Respuesta, myListRespuesta: ArrayList<Respuesta>, NotifyUpdateRecy: NotifyUpdateRecyclerView)
        {
            counter.text=adapterPosition.toString()
            descripcion.text=respuesta.descripcion

            if(respuesta.correcta!!)
                iconCorecto.setImageResource(R.drawable.ic_check_correcto)
            else
                iconCorecto.setImageResource(R.drawable.ic_incorrecto)

            iconQuitar.setOnClickListener {
                myListRespuesta.remove(respuesta)

                if(myListRespuesta.size==0)
                    AgregarRespuestaActivity.puedeGuardar=false

                NotifyUpdateRecy.updateRecy()
            }
        }
    }
}
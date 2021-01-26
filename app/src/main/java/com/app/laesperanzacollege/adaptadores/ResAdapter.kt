package com.app.laesperanzacollege.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.R
import com.app.laesperanzacollege.interfaces.NotifyUpdateRecyclerView
import com.app.laesperanzaedm.model.Respuesta
import kotlinx.android.synthetic.main.item_list_preguntaresp.view.*

class ResAdapter(var myListRespuesta:ArrayList<Respuesta>, var notifyInterface: NotifyUpdateRecyclerView):RecyclerView.Adapter<ResAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView= LayoutInflater.from(parent.context).inflate(R.layout.item_list_preguntaresp,parent,false)
        return MyViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return myListRespuesta.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindHolder(myListRespuesta[position],myListRespuesta,notifyInterface)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var descripcion=itemView.textResp
        var iconCorrecto:ImageView=itemView.iconCorrecto
        var iconBorrar:ImageView=itemView.imgDelete

        fun bindHolder(respuesta: Respuesta,myListRespuesta: ArrayList<Respuesta>,notifyInterface: NotifyUpdateRecyclerView)
        {
            descripcion.text=respuesta.descripcion

            if(respuesta.correcta!!)
                iconCorrecto.setBackgroundResource(R.drawable.ic_check_correcto)
            else
                iconCorrecto.setImageResource(R.drawable.ic_incorrecto)

            iconBorrar.setOnClickListener {
                //Buscar en la Bd y eliminarla tambien
                myListRespuesta.remove(respuesta)
                notifyInterface.updateRecy()
            }
        }
    }
}
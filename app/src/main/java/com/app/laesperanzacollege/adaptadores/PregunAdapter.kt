package com.app.laesperanzacollege.adaptadores

import Observers.QuizPreguntaAdapterObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.R
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzadao.RespuestaDAO
import com.app.laesperanzaedm.model.Pregunta
import kotlinx.android.synthetic.main.item_list_quizzpregunta.view.*

class PregunAdapter(val myListPreguntas:ArrayList<Pregunta>,var notifyter:QuizPreguntaAdapterObserver): RecyclerView.Adapter<PregunAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView= LayoutInflater.from(parent.context).inflate(R.layout.item_list_quizzpregunta,parent,false)
        return MyViewHolder(myView)
    }

    override fun getItemCount(): Int {
       return myListPreguntas.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindHolder(myListPreguntas[position],notifyter,myListPreguntas)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var descripcion=itemView.txtPreg
        var iconBorrar=itemView.imgDeletePreg

        fun bindHolder(
            pregunta: Pregunta,
            notifyter: QuizPreguntaAdapterObserver,
            myListPreguntas: ArrayList<Pregunta>
        )
        {
            descripcion.text=pregunta.descripcion
            iconBorrar.setOnClickListener {
                if(PreguntaDAO(itemView.context).Eliminar(pregunta.id))
                {
                    myListPreguntas.remove(pregunta)
                    //si se elimino la pregunta se deben eliminar todas las respuestas asociadas a esa pregunta
                    RespuestaDAO(itemView.context).Eliminar(pregunta.id)
                    notifyter.deleteRecyItem(pregunta)
                }
                else
                    Toast.makeText(itemView.context,"No se pudo eliminar",Toast.LENGTH_LONG).show()
            }
        }
    }
}
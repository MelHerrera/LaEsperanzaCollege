package com.app.laesperanzacollege.adaptadores

import Observers.UnidadObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.R
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzaedm.model.Unidad
import kotlinx.android.synthetic.main.item_unidades.view.*


class UnidAdapter(var listUnidades:ArrayList<Unidad>):
    RecyclerView.Adapter<UnidAdapter.myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        var myView=LayoutInflater.from(parent.context).inflate(R.layout.item_unidades,parent,false)
        return myViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return listUnidades.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
       holder.bindItem(listUnidades[position])
    }

    class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var myQuizDAO=QuizDAO(itemView.context)
        fun bindItem(myUnidad: Unidad) {
            val descripcion = itemView.txtDesc

            val numUnidad = itemView.txtNumUnidad
            val cardUnidad=itemView.viewUnidad
            val cantidadPreguntas=itemView.cantPreguntas

            cardUnidad.isChecked=false
            descripcion.text = myUnidad.descripcion
            numUnidad.text = myUnidad.numUnidad.toString()
            cantidadPreguntas.text=obtenerCantidad(myUnidad.numUnidad!!)

            cardUnidad.setOnLongClickListener {
                cardUnidad.isChecked = !cardUnidad.isChecked

                if(cardUnidad.isChecked)
                {
                   myUnidadObserver?.startSelection(adapterPosition,true)
                }
                else
                    myUnidadObserver?.startSelection(adapterPosition,false)

                false
            }
        }

        fun obtenerCantidad(numUnidad: Int): String? {

            val result=myQuizDAO.CantQuizzes(numUnidad)

            return result.toString()
        }
    }

    companion object
    {
        var myUnidadObserver:UnidadObserver?=null
    }
}
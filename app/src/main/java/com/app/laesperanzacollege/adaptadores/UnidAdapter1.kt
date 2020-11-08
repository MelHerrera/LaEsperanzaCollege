package com.app.laesperanzacollege.adaptadores

import Observers.UnidadObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.R
import com.app.laesperanzaedm.model.Unidad
import kotlinx.android.synthetic.main.activity_agregar__unidad.view.*
import kotlinx.android.synthetic.main.item_unidad.view.*
import kotlinx.android.synthetic.main.item_unidades.view.txtDesc
import kotlinx.android.synthetic.main.item_unidades.view.txtNumUnidad
import kotlinx.android.synthetic.main.item_unidades.view.viewUnidad

class UnidAdapter1(var listUnidades:ArrayList<Unidad>):
    RecyclerView.Adapter<UnidAdapter1.myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        var myView=LayoutInflater.from(parent.context).inflate(R.layout.item_unidad,parent,false)
        return myViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return listUnidades.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.bindItem(listUnidades[position])
    }

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(myUnidad: Unidad) {
            var descripcion = itemView.txtDesc
            var numUnidad = itemView.txtNumUnidad
            var cardUnidad=itemView.viewUnidad

            cardUnidad.isChecked=false
            descripcion.text = myUnidad.descripcion
            numUnidad.text = myUnidad.numUnidad.toString()


                cardUnidad.setOnLongClickListener {

                    if(intChecked<1 || Integer.parseInt(this.itemView.txtNumUnidad.text.toString())==allowCardChecked)
                    {
                        cardUnidad.toggle()

                        if(cardUnidad.isChecked)
                        {
                            intChecked++
                            allowCardChecked=Integer.parseInt(this.itemView.txtNumUnidad.text.toString())
                            myUnidadObserver?.startSelection(adapterPosition,true)
                        }
                        else
                        {
                            intChecked--
                            allowCardChecked=null
                            myUnidadObserver?.startSelection(adapterPosition,false)
                        }
                    }
                    else
                        Toast.makeText(itemView.context,"No puedes Seleccionar Mas de una Unidad",Toast.LENGTH_SHORT).show()

                    false
                }

        }
    }

    companion object
    {
        var myUnidadObserver:UnidadObserver?=null
        var allowCardChecked:Int?=null
        var intChecked:Int=0
    }
}
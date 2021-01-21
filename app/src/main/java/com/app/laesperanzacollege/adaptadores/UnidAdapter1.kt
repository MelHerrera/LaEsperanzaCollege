package com.app.laesperanzacollege.adaptadores

import Observers.FiltroObserver
import Observers.UnidadObserver
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.AgregarQuizActivity
import com.app.laesperanzacollege.R
import com.app.laesperanzadao.GradoDAO
import com.app.laesperanzadao.UnidadDAO
import com.app.laesperanzaedm.model.Unidad
import kotlinx.android.synthetic.main.item_unidades.view.txtDesc
import kotlinx.android.synthetic.main.item_unidades.view.txtNumUnidad
import kotlinx.android.synthetic.main.item_unidades.view.viewUnidad
import kotlin.collections.ArrayList

class UnidAdapter1(var listUnidades:ArrayList<Unidad>, var checkPos:Int):
    RecyclerView.Adapter<UnidAdapter1.myViewHolder>(),Filterable {
    var listaAuxDeUnidades= arrayListOf<Unidad>()
    var myContext:Context?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val myView=LayoutInflater.from(parent.context).inflate(R.layout.item_unidad,parent,false)
        myContext=myView.context
        listaAuxDeUnidades=UnidadDAO(myContext!!).listarUnidades()
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
            val descripcion = itemView.txtDesc
            val numUnidad = itemView.txtNumUnidad
            val cardUnidad=itemView.viewUnidad

            cardUnidad.isChecked=false
            descripcion.text = myUnidad.descripcion
            numUnidad.text = myUnidad.numUnidad.toString()

            if(adapterPosition==checkPos)
                cardUnidad.isChecked=true

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
                            intChecked=0
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
        var myFilterObserver:FiltroObserver?=null
    }

    override fun getFilter(): Filter {
        return object : Filter()
        {
            override fun performFiltering(wordToSearch: CharSequence?): FilterResults {
                val myFilter= FilterResults()


                if(wordToSearch?.length==0 || wordToSearch?.equals("Todos")!!)
                {
                    myFilter.values=listaAuxDeUnidades
                }
                else
                {
                    val codGrado=GradoDAO(myContext!!).buscarGrado(wordToSearch.toString())
                    myFilter.values = listaAuxDeUnidades.filter { x-> x.codGrado==codGrado}
                }
                return myFilter
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                if(results?.values!=null)
                {
                    listUnidades.clear()
                    listUnidades.addAll(results.values as Collection<Unidad>)
                    notifyDataSetChanged()

                    if(listUnidades.size>0)
                    {
                        myFilterObserver?.filterElements(true)
                    }
                    else
                        myFilterObserver?.filterElements(false)
                }
            }
        }
    }
}
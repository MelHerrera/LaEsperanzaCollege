package com.app.laesperanzacollege.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.R
import kotlinx.android.synthetic.main.item_curiosidad.view.*

class CurAdapter (val list:ArrayList<Cur>, val context: Context):
    RecyclerView.Adapter<CurAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: CurAdapter.ViewHolder, position: Int) {
        holder.bindItem(list[position])

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_curiosidad,parent,false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(Cur:Cur){
            var name: TextView = itemView.findViewById(R.id.name) as TextView
            var texto: TextView = itemView.findViewById(R.id.texto) as TextView
            var foto: ImageView = itemView.findViewById(R.id.foto) as ImageView
            name.text = Cur.name
            texto.text = Cur.texto
            foto.setImageResource(Cur.foto!!)
        }

    }
}
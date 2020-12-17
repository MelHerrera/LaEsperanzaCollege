package com.app.laesperanzacollege.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.Intro
import com.app.laesperanzacollege.R

class introAdapter(private val Intro:List<Intro>)
    : RecyclerView.Adapter<introAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: introAdapter.ViewHolder, position: Int) {
        holder.bindItem(Intro[position])

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  introAdapter.ViewHolder {
        return  ViewHolder(
            LayoutInflater.from(parent.context).inflate(
            R.layout.item_intro,parent,false
        ))
    }

    override fun getItemCount(): Int {
        return  Intro.size
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name1) as TextView
        private val des: TextView = itemView.findViewById(R.id.des) as TextView
        private val foto1: ImageView = itemView.findViewById(R.id.foto1) as ImageView
        fun bindItem(Intro: Intro){

            name.text = Intro.name
            des.text = Intro.des
            foto1.setImageResource(Intro.foto!!)
        }

    }
}
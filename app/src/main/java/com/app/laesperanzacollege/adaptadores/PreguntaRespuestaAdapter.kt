package com.app.laesperanzacollege.adaptadores

import Observers.RespuestaObserver
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.AgregarRespuestaActivity
import com.app.laesperanzacollege.R
import com.app.laesperanzacollege.interfaces.NotifyUpdateRecyclerView
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Respuesta
import kotlinx.android.synthetic.main.item_preguntas_expandable.view.*

class PreguntaRespuestaAdapter(var myListPreguntas:ArrayList<Pregunta>,var myListRespuesta: ArrayList<Respuesta>): RecyclerView.Adapter<PreguntaRespuestaAdapter.MyVieHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVieHolder {
       val myView=LayoutInflater.from(parent.context).inflate(R.layout.item_preguntas_expandable,parent,false)
        return MyVieHolder(myView)
    }

    override fun getItemCount(): Int {
        return myListPreguntas.size
    }

    override fun onBindViewHolder(holder: MyVieHolder, position: Int) {
        val myListRespuestaFilter:ArrayList<Respuesta> =ArrayList()

        for (item in myListRespuesta)
        {
            if(item.preguntaId==myListPreguntas[position].id)
                myListRespuestaFilter.add(item)
        }

       holder.bindItem(myListPreguntas[position],myListRespuestaFilter)
    }

    class MyVieHolder(itemView: View) : RecyclerView.ViewHolder(itemView),NotifyUpdateRecyclerView, RespuestaObserver
    {
        var myRespuestaAdapter: ResAdapter?=null
        var mListRespuestas:ArrayList<Respuesta> = arrayListOf()
        var myPregunta=itemView.textPre
        var myExpandableIcon=itemView.iconExpand
        var myExpandableView=itemView.viewExpanded
        var myRecyRespuesta=itemView.recyPregRes
        var myIconAdd=itemView.imgAdd
        var cantidadElementos=itemView.msjCantidad
        var preguntaCounter=itemView.imgIconPreguntaCounter

        init {
           myExpandableView.visibility=View.GONE
            myExpandableIcon.setBackgroundResource(R.drawable.ic_expand_more)
        }

        fun bindItem(pregunta: Pregunta,myListRespuesta: ArrayList<Respuesta>) {
            myPregunta.text=pregunta.descripcion
            preguntaCounter.text=adapterPosition.toString()
            this.mListRespuestas=myListRespuesta

            myExpandableIcon.setOnClickListener {
                val expanded=myExpandableView.visibility

                if(expanded==View.VISIBLE)
                {
                    myExpandableView.visibility=View.GONE
                    myExpandableIcon.setBackgroundResource(R.drawable.ic_expand_more)
                }
                else
                {
                    myExpandableView.visibility=View.VISIBLE
                    myExpandableIcon.setBackgroundResource(R.drawable.ic_expand_less)
                }

                if (myListRespuesta.size==0)
                {
                    cantidadElementos.visibility=View.VISIBLE
                    cantidadElementos.text="No hay respuestas aun"
                }
                else
                {
                    cantidadElementos.visibility=View.GONE
                    cantidadElementos.text = ""
                }
            }

            myRecyRespuesta.layoutManager=LinearLayoutManager(itemView.context)
            val myItemDecoration= DividerItemDecoration(itemView.context,DividerItemDecoration.VERTICAL)
            myRecyRespuesta.addItemDecoration(myItemDecoration)
            myRespuestaAdapter=ResAdapter(myListRespuesta,this)
            myRecyRespuesta.adapter=myRespuestaAdapter

            myIconAdd.setOnClickListener {
                val myIntent=Intent(itemView.context,AgregarRespuestaActivity::class.java)
                myIntent.putExtra("PREGUNTA",pregunta)
                myIntent.putExtra("OPERACION",OperacionesCrud.Editar)
                AgregarRespuestaActivity.myRespuestaObserver=this
                itemView.context.startActivity(myIntent)
            }
        }

        override fun updateRecy() {
            myRespuestaAdapter?.notifyDataSetChanged()
        }

        override fun respuestaSaved(newRespuestas: ArrayList<Respuesta>) {
            mListRespuestas.addAll(newRespuestas)
            updateRecy()
        }

        override fun preguntaSaved(pregunta: Pregunta, mListRespuestas: ArrayList<Respuesta>) {
           //nothing to do
        }
    }
}

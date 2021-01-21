package com.app.laesperanzacollege.adaptadores

import Observers.PreguntaObserver
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.AgregarPreguntaActivity
import com.app.laesperanzacollege.R
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Quiz
import kotlinx.android.synthetic.main.item_list_quiz.view.*

class QuizPreguntaAdapter(var myListQuiz:ArrayList<Quiz>, var myListPregunta:ArrayList<Pregunta>)
    :RecyclerView.Adapter<QuizPreguntaAdapter.MyVieHolder>() {
    var myPreguntaAdapter: PregunAdapter?=null
    var viewCantidadDeElementos:TextView?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVieHolder {
        val myView= LayoutInflater.from(parent.context).inflate(R.layout.item_list_quiz,parent,false)
        return MyVieHolder(myView)
    }

    override fun getItemCount(): Int {
        return myListQuiz.size
    }

    override fun onBindViewHolder(holder: MyVieHolder, position: Int) {
        val myListPreguntaFilter:ArrayList<Pregunta> =ArrayList()
        for (item in myListPregunta)
        {
            if(item.quizzId==myListQuiz[position].quizId)
                myListPreguntaFilter.add(item)
        }

        holder.bindItem(myListQuiz[position],myListPreguntaFilter)
    }

  inner class MyVieHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var myPreguntaDAO:PreguntaDAO?=null
        var myQuiz=itemView.textQuiz
        var myExpandableIcon=itemView.iconExpandQuiz
        var myExpandableView=itemView.viewExpandedQuiz
        var myRecyRespuesta=itemView.recyQuizResp
        var myIconAdd=itemView.imgAddQuiz
        var myQuizCount=itemView.imgIconQuizCounter

        init {
            myPreguntaDAO= PreguntaDAO(itemView.context)
            myExpandableView.visibility= View.GONE
            myExpandableIcon.setBackgroundResource(R.drawable.ic_expand_more)
            viewCantidadDeElementos=itemView.msjCantidadPreguntas
        }

        fun bindItem(quiz:Quiz, myListPregunta: ArrayList<Pregunta>) {
            myQuiz.text=quiz.nombre
            myQuizCount.text=adapterPosition.toString()

            myExpandableIcon.setOnClickListener {
                val expanded=myExpandableView.visibility

                if(expanded== View.VISIBLE)
                {
                    myExpandableView.visibility= View.GONE
                    myExpandableIcon.setBackgroundResource(R.drawable.ic_expand_more)
                }
                else
                {
                    myExpandableView.visibility= View.VISIBLE
                    myExpandableIcon.setBackgroundResource(R.drawable.ic_expand_less)
                }
            }

            if (myListPregunta.size==0)
            {
                viewCantidadDeElementos?.visibility= View.VISIBLE
                viewCantidadDeElementos?.text="No Hay Preguntas Aun"
            }
            else
            {
                viewCantidadDeElementos?.visibility= View.GONE
                viewCantidadDeElementos?.text = ""
            }

            myRecyRespuesta.layoutManager= LinearLayoutManager(itemView.context)
            myPreguntaAdapter=PregunAdapter(myListPregunta)
            val myItemDecoration= DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL)
            myRecyRespuesta.addItemDecoration(myItemDecoration)
            myRecyRespuesta.adapter=myPreguntaAdapter

            myIconAdd.setOnClickListener {
                val myIntent= Intent(itemView.context, AgregarPreguntaActivity::class.java)
                myIntent.putExtra("QUIZZID",quiz.quizId)
                myIntent.putExtra("QUIZZNOM",quiz.nombre)
                itemView.context.startActivity(myIntent)
            }
        }
    }
    companion object
    {
        var myPreguntaObserver:PreguntaObserver?=null
    }
}
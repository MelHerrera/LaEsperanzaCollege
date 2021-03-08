package com.app.laesperanzacollege.adaptadores

import Observers.QuizPreguntaAdapterObserver
import Observers.QuizesAdapterObserver
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.AgregarPreguntaActivity
import com.app.laesperanzacollege.R
import com.app.laesperanzacollege.interfaces.NotifyUpdateRecyclerView
import com.app.laesperanzadao.PreguntaDAO
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Quiz
import kotlinx.android.synthetic.main.item_list_quiz.view.*

class QuizPreguntaAdapter(var myListQuiz:ArrayList<Quiz>, private var myListPregunta:ArrayList<Pregunta>, val mOperacion:String)
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

        holder.bindItem(myListQuiz[position],myListPreguntaFilter,mOperacion)
    }

  inner class MyVieHolder(itemView: View) : RecyclerView.ViewHolder(itemView),QuizPreguntaAdapterObserver
    {
        private var myPreguntaDAO:PreguntaDAO?=null
        private var myQuiz: TextView =itemView.textQuiz
        private var myExpandableIcon: ImageView =itemView.iconExpandQuiz
        private var myExpandableView: ConstraintLayout =itemView.viewExpandedQuiz
        private var myRecyRespuesta: RecyclerView =itemView.recyQuizResp
        private var myIconAdd: ImageView =itemView.imgAddQuiz
        private var myQuizCount: TextView =itemView.imgIconQuizCounter

        init {
            myPreguntaDAO= PreguntaDAO(itemView.context)
            myExpandableView.visibility= View.GONE
            myExpandableIcon.setBackgroundResource(R.drawable.ic_expand_more)
            viewCantidadDeElementos=itemView.msjCantidadPreguntas
        }

        fun bindItem(
            quiz: Quiz,
            myListPregunta: ArrayList<Pregunta>,
            mOperacion: String
        ) {

            if(mOperacion==OperacionesCrud.Editar.toString())
                myIconAdd.setImageResource(R.drawable.ic_edit_purple)
            else
                myIconAdd.setImageResource(R.drawable.ic_add_circle)

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
            validar(myListPregunta,itemView.context)

            myRecyRespuesta.layoutManager= LinearLayoutManager(itemView.context)
            myPreguntaAdapter=PregunAdapter(myListPregunta,this)
            val myItemDecoration= DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL)
            myRecyRespuesta.addItemDecoration(myItemDecoration)
            myRecyRespuesta.adapter=myPreguntaAdapter

            myIconAdd.setOnClickListener {
                val myIntent= Intent(itemView.context, AgregarPreguntaActivity::class.java)
                myIntent.putExtra("QUIZZ",quiz)
                myIntent.putExtra("OPERACION",mOperacion)
                itemView.context.startActivity(myIntent)
            }
        }

        fun validar(myListPregunta: ArrayList<Pregunta>, context: Context)
        {
            if (myListPregunta.size==0)
            {
                viewCantidadDeElementos?.visibility= View.VISIBLE
                viewCantidadDeElementos?.text=context.getString(R.string.sin_datos,context.getString(R.string.preguntas))
            }
            else
            {
                viewCantidadDeElementos?.visibility= View.GONE
                viewCantidadDeElementos?.text = ""
            }
        }

        fun updateRecyclerView()
        {
            myPreguntaAdapter?.notifyDataSetChanged()
            validar(myListPregunta,itemView.context)
        }

        override fun deleteRecyItem(item: Pregunta) {
            myListPregunta.remove(item)
            updateRecyclerView()
        }
    }
}
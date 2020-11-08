package com.app.laesperanzacollege.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.R
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzaedm.model.Quiz
import kotlinx.android.synthetic.main.item_quizzes.view.*

class QuizzesAdapter(var myListQuiz:ArrayList<Quiz>):
    RecyclerView.Adapter<QuizzesAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var myView=LayoutInflater.from(parent.context).inflate(R.layout.item_quizzes,parent,false)
        return MyViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return myListQuiz.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.BindItem(myListQuiz[position])
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var myQuizDAO=QuizDAO(itemView.context)
        var nombre=itemView.txtNombre
        var cantPreguntas=itemView.cantPreguntas
        var quizzEstado=itemView.quizzEstado


        fun BindItem(myQuizz:Quiz)
        {
            nombre.text=myQuizz.nombre
            cantPreguntas.text=ObtenerCantidad(myQuizz.quizId!!)
            quizzEstado.text=ObtenerEstado(myQuizz.quizId!!)
        }

        private fun ObtenerEstado(quizId: Int): String {

            var result=myQuizDAO.EstadoQuizz(quizId)

            if(result==0)
                return "Pendiente"
            else
                return "Finalizado"
        }

        private fun ObtenerCantidad(quizId: Int): String? {

            var result=myQuizDAO.CantPreguntas(quizId)

            return result.toString()
        }
    }
}
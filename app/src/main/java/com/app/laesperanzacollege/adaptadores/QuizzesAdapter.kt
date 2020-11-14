package com.app.laesperanzacollege.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        var btnPractica=itemView.btnPractica

        fun BindItem(myQuizz:Quiz)
        {
            nombre.text=myQuizz.nombre
            cantPreguntas.text=ObtenerCantidad(myQuizz.quizId!!)
            quizzEstado.text=ObtenerEstado(myQuizz.quizId!!)

            btnPractica.setOnClickListener {
                val estado=ObtenerEstado(myQuizz.quizId!!)

                when(estado)
                {
                    "No Iniciado"->
                    {
                        val result=myQuizDAO.enProgreso(myQuizz.quizId!!)
                        if(result)
                        {
                            btnPractica.text="Finalizar"
                            quizzEstado.text="En Progreso"
                        }
                    }
                    "Finalizado"->
                    {
                        btnPractica.isEnabled=false
                        quizzEstado.text="Finalizado"
                    }
                    "En Progreso"->
                    {
                        val result=myQuizDAO.finalizar(myQuizz.quizId!!)
                        if(result)
                        {
                            btnPractica.text="Finalizado"
                            btnPractica.isEnabled=false
                            quizzEstado.text="Finalizado"
                        }
                    }
                }

            }
        }

        private fun ObtenerEstado(quizId: Int): String {

            var result=myQuizDAO.EstadoQuizz(quizId)

            if(result==0)
            {
                btnPractica.text="Iniciar"
                return "No Iniciado"
            }
            else
                if(result==-1)
                {
                    btnPractica.text="Finalizado"
                    btnPractica.isEnabled=false
                    return "Finalizado"
                }
                else
                {
                    btnPractica.text="Finalizar"
                    return "En Progreso"
                }

        }

        private fun ObtenerCantidad(quizId: Int): String? {

            var result=myQuizDAO.CantPreguntas(quizId)

            return result.toString()
        }
    }
}
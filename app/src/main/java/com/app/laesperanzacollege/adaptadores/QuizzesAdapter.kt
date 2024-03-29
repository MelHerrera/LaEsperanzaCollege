package com.app.laesperanzacollege.adaptadores

import Observers.QuizzObserver
import android.app.Activity
import android.content.Intent
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.R
import com.app.laesperanzacollege.TestActivity
import com.app.laesperanzadao.QuizDAO
import com.app.laesperanzadao.enums.TipoDeUsuarios
import com.app.laesperanzadao.enums.TipodeTest
import com.app.laesperanzaedm.model.Quiz
import kotlinx.android.synthetic.main.item_quizzes.view.*
import java.util.*

class QuizzesAdapter(var myListQuiz:ArrayList<Quiz>, private var tipoDeUsuario:TipoDeUsuarios, private var usuarioId:Int,var mOperacion:TipodeTest?):
    RecyclerView.Adapter<QuizzesAdapter.MyViewHolder>(), Filterable {
    var myQuizFilterList= arrayListOf<Quiz>()

    init {
        myQuizFilterList.addAll(myListQuiz)
    }
    companion object
    {
        var myListQuizzes:ArrayList<View> = arrayListOf()
        var myQuizzObserver: QuizzObserver?=null
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView=LayoutInflater.from(parent.context).inflate(R.layout.item_quizzes,parent,false)
        return MyViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return myListQuiz.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItem(myListQuiz[position],tipoDeUsuario,usuarioId,mOperacion!!)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private var myQuizDAO=QuizDAO(itemView.context)
        var nombre: TextView =itemView.txtNombre
        private var cantPreguntas: TextView =itemView.cantPreguntas
        private var quizzEstado: TextView =itemView.quizzEstado
        private var btnPractica: Button =itemView.btnPractica
        private var cardView=itemView.cardView_Quiz

        fun bindItem(
            myQuizz: Quiz,
            tipoDeUsuario: TipoDeUsuarios,
            usuarioId: Int,
            mOperacion: TipodeTest?
        )
        {
            nombre.text=myQuizz.nombre
            cantPreguntas.text=obtenerCantidad(myQuizz.quizId!!).toString()
            quizzEstado.text=establecerEstado(myQuizz.quizId!!)

            myListQuizzes.add(itemView)

            if(tipoDeUsuario==TipoDeUsuarios.Admin)
            {
               ObtenerEstado(myQuizz.quizId!!)
            }

            cardView.isChecked=false

            //action mode to edit and delete
            cardView.setOnLongClickListener {

                if(tipoDeUsuario==TipoDeUsuarios.Admin)
                {
                    //chechear o descheckear el cardview
                    cardView.isChecked = !cardView.isChecked

                    if(cardView.isChecked)
                       myQuizzObserver?.quizSelection(adapterPosition,true)
                    else
                        myQuizzObserver?.quizSelection(adapterPosition,false)


                    //iniciar el menu action Mode
                    myQuizzObserver?.actionModeInit()
                }

                return@setOnLongClickListener false
            }

            btnPractica.setOnClickListener {

                if(tipoDeUsuario==TipoDeUsuarios.Admin)
                {
                    when(ObtenerEstado(myQuizz.quizId!!))
                    {
                        itemView.context.getString(R.string.txt_noiniciado)->
                        {
                            val result=myQuizDAO.enProgreso(myQuizz.quizId!!)
                            if(result)
                            {
                                btnPractica.text=itemView.context.getString(R.string.txt_finalizar)
                                quizzEstado.text=itemView.context.getString(R.string.txt_enprogreso)
                            }
                        }
                        itemView.context.getString(R.string.finalizado)->
                        {
                            btnPractica.isEnabled=false
                            quizzEstado.text=itemView.context.getString(R.string.finalizado)
                        }
                        itemView.context.getString(R.string.txt_enprogreso)->
                        {
                            val result=myQuizDAO.finalizar(myQuizz.quizId!!)
                            if(result)
                            {
                                btnPractica.text=itemView.context.getString(R.string.finalizado)
                                btnPractica.isEnabled=false
                                quizzEstado.text=itemView.context.getString(R.string.finalizado)
                            }
                        }
                    }
                }
                else
                {
                    val myIntent=Intent(itemView.context, TestActivity::class.java)
                    myIntent.putExtra(itemView.context.getString(R.string.txt_tipoTest),mOperacion)
                    myIntent.putExtra(itemView.context.getString(R.string.keyNameQuiz),myQuizz)
                    myIntent.putExtra(itemView.context.getString(R.string.keyNameUser),usuarioId)
                    itemView.context.startActivity(myIntent)
                }
            }
        }

        private fun ObtenerEstado(quizId: Int): String
        {
            val result=myQuizDAO.estadoQuizz(quizId)

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

        private fun establecerEstado(quizId:Int):String
        {
            val result=myQuizDAO.estadoQuizz(quizId)

            if(result==0)
            {
                return "No Iniciado"
            }
            else
                if(result==-1)
                {
                    return "Finalizado"
                }
                else
                {
                    return "En Progreso"
                }
        }

        private fun obtenerCantidad(quizId: Int): Int {

            return myQuizDAO.cantPreguntas(quizId)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter()
        {
            override fun performFiltering(wordToSearch: CharSequence?): FilterResults {
                val myFilter= FilterResults()

                if(wordToSearch?.length==0)
                {
                    myFilter.values=myQuizFilterList
                }
                else
                {
                    myFilter.values = myListQuiz.filter { x->x.nombre?.toUpperCase(Locale.ROOT)!!.contains(wordToSearch.toString()) }
                }
                return myFilter
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
             myListQuiz.clear()
                myListQuiz.addAll(results?.values as Collection<Quiz>)
                notifyDataSetChanged()
            }

        }
    }
}
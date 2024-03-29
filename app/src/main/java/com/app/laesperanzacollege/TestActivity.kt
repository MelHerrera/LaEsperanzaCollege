package com.app.laesperanzacollege

import Observers.ViewPagerObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.app.laesperanzacollege.fragmentos.PruebaFragment
import com.app.laesperanzadao.UsuarioQuizzDAO
import com.app.laesperanzadao.enums.EstadoQuiz
import com.app.laesperanzadao.enums.OperacionesCrud
import com.app.laesperanzadao.enums.TipodeTest
import com.app.laesperanzaedm.model.Quiz
import com.app.laesperanzaedm.model.UsuarioQuiz

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val quiz=intent.extras?.get(getString(R.string.keyNameQuiz)) as Quiz?
        val usuarioId=intent.extras?.get(getString(R.string.keyNameUser)) as Int
        val mOpe=intent.extras?.get(getString(R.string.txt_tipoTest)) as TipodeTest

        supportActionBar?.elevation=0.0f
        supportActionBar?.title=""

        if(quiz!=null && usuarioId>0)
        {
            //Indicar el usuario que realiza el quiz y como apenas se inicia dejar el estado como pendiente
            //Antes se verifica que el quiz aun este pendiente
            if(quiz.quizId!=null)
            {
                if(!usuarioQuizFinalizado(quiz.quizId!!) || mOpe==TipodeTest.Practica)
                {
                    val mUsuarioQuiz = UsuarioQuiz()
                    mUsuarioQuiz.QuizId=quiz.quizId
                    mUsuarioQuiz.UsuarioId=usuarioId
                    mUsuarioQuiz.Estado=EstadoQuiz.Pendiente.ordinal

                    if(UsuarioQuizzDAO(this).existe(usuarioId, quiz.quizId!!)==null)
                     UsuarioQuizzDAO(this).insertar(mUsuarioQuiz)

                    val myPruebaFragment=PruebaFragment()
                    val myData=Bundle()
                    myData.putSerializable(getString(R.string.keyNameQuiz),quiz)
                    myData.putInt(getString(R.string.keyNameUser),usuarioId)
                    myData.putSerializable(getString(R.string.keynameUsuarioQuiz),UsuarioQuizzDAO(this).existe(usuarioId, quiz.quizId!!))
                    myData.putSerializable(getString(R.string.txt_tipoTest),mOpe)

                    myPruebaFragment.arguments=myData
                    supportFragmentManager.beginTransaction().add(R.id.content,myPruebaFragment,null).commit()
                }
                else
                    this.finish()
            }
        }
    }

    private fun usuarioQuizFinalizado(quizzId:Int):Boolean
    {
        return UsuarioQuizzDAO(this).usuarioQuizzEstado(quizzId)
    }
    companion object
    {
        var myViewPagerObserver:ViewPagerObserver?=null
    }
    override fun onBackPressed() {
        if(myViewPagerObserver?.estaEnPaginaPrimera()!!)
        {
            val myAlert= AlertDialog.Builder(this)

            myAlert.setTitle("Abandonar Prueba")
            myAlert.setMessage("Si Sales Perderas el avance. ¿Esta Seguro que desea Abandonar la Prueba?")
            myAlert.setIcon(android.R.drawable.ic_dialog_alert)

            myAlert.setPositiveButton("Abandonar") { _, _ ->
                super.onBackPressed()
                this.finish()
            }

            myAlert.setNegativeButton("Cancelar") { _, _ ->
                myAlert.create().dismiss()
            }

            myAlert.show()
        }
        else
            myViewPagerObserver?.paginaAnterior()
    }
}
package com.app.laesperanzacollege

import Observers.MainViewPagerObserver
import Observers.ViewPagerObserver
import android.app.ActionBar
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.Utils.Companion.letrasEditTextEstaLlena
import com.app.laesperanzacollege.Utils.Companion.mostrarContiuar
import com.app.laesperanzacollege.fragmentos.PruebaFragment
import com.app.laesperanzadao.RespuestaDAO
import com.app.laesperanzadao.ResultadoDAO
import com.app.laesperanzadao.UsuarioQuizzDAO
import com.app.laesperanzadao.enums.EstadoQuiz
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Respuesta
import com.app.laesperanzaedm.model.ResultadoQuiz
import com.app.laesperanzaedm.model.UsuarioQuiz
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.alert_finalizarquiz.view.*
import kotlinx.android.synthetic.main.mainquizfragment.view.*
import java.util.*
import kotlin.random.Random


class MainQuizFragment(private var myPpregunta:Pregunta, private var actual:Int, private var final:Int, private var usuarioId:Int,
                       private var usuarioQuizId:Int)
    : Fragment(), MainViewPagerObserver
{
    private var myOpciones:GridView?=null
    private var btnContinuar:Button?=null
    private var respuestasSelected:ArrayList<Int>?= arrayListOf()
    private var letrasEditText:ArrayList<EditText> = arrayListOf()
    private var letrasButton:ArrayList<Button> = arrayListOf()
    private var txtSinRespuesta:TextView?=null
    private var viewMostrarRespuestas:LinearLayoutCompat?=null
    private var finalizar:Boolean=false
    private var finalizado:Boolean=false
    private var revisar:Boolean=false

    companion object
    {
        var myViewPagerObserver:ViewPagerObserver?=null
        var quizRespuestas:ArrayList<ResultadoQuiz>?= arrayListOf()
        var listPreguntas:ArrayList<Pregunta> = arrayListOf()
        var myFrags:ArrayList<View>?= arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myFrag=inflater.inflate(R.layout.mainquizfragment,container,false)

        myFrag.pregunta.text=myPpregunta.descripcion
        listPreguntas.add(myPpregunta)
        myFrags?.add(myFrag)

        myFrag.indicadorPreguntaActual.text=actual.toString()
        myFrag.indicadorPreguntaFinal.text=final.toString()
        txtSinRespuesta=myFrag.sinResp
        viewMostrarRespuestas=myFrag.viewMostrarResp

        myOpciones=myFrag.opciones
        btnContinuar=myFrag.btnContinuar

        if(ultimaPregunta(actual,final))
        {
                myFrag.btnContinuar.text = context?.getString(R.string.txt_finaliza)
                finalizar=true
        }
        else
        {
            myFrag.btnContinuar.text=context?.getString(R.string.txt_siguiente)
            finalizar=false
        }


        //set this theme for use Chip and ChipGroup
        activity!!.applicationContext.setTheme(R.style.Theme_MaterialComponents)

        opcionRespuesta(myPpregunta.opcionDeRespuestaId,respuestas(myPpregunta.id),myFrag.viewRespuestas)

        myFrag.btnContinuar.setOnClickListener {
            //antes de pasar a la pagina siguiente o finalizar se debe guardar las selecciones de esta pagina
            val pageResultado=ResultadoQuiz()
            pageResultado.usuarioQuizId=usuarioQuizId
            pageResultado.preguntaId=myPpregunta.id
            pageResultado.respuestasId=respuestasSelected

           val pageResulExiste= quizRespuestas?.find { x->x.preguntaId==myPpregunta.id }

            if(pageResulExiste!=null)
            {
                quizRespuestas?.set(actual-1, pageResultado)
            }
            else
            quizRespuestas?.add(pageResultado)

            if(finalizar) {
                if(!revisar)
                {
                    //Mandar el quiz y el usuario y que se cambie a estado finalizado
                    val mUsuarioQuiz = UsuarioQuiz()
                    mUsuarioQuiz.QuizId=myPpregunta.quizzId
                    mUsuarioQuiz.UsuarioId=usuarioId
                    mUsuarioQuiz.Estado= EstadoQuiz.Finalizado.ordinal

                    if(UsuarioQuizzDAO(context!!).actualizar(mUsuarioQuiz))
                    {
                        //enviar rsultados a la tabla de resultados
                        if(quizRespuestas!=null)
                        {
                            if(quizRespuestas!!.size>0)
                            {
                                if(!finalizado)
                                {
                                    quizRespuestas!!.forEach {
                                        ResultadoDAO(context!!).guardarResultados(it)
                                    }
                                    finalizado=true
                                }


                                val mAlert= AlertDialog.Builder(context!!).create()
                                mAlert.setTitle("Finalizado")
                                val mAlertView= LayoutInflater.from(context).inflate(R.layout.alert_finalizarquiz,null)
                                mAlert.setView(mAlertView)

                                mAlertView.AlertTer.setOnClickListener {
                                    mAlert.dismiss()
                                    activity!!.finish()
                                }

                                mAlertView.AlertRev.setOnClickListener {
                                    mAlert.dismiss()
                                    revisar=true
                                    if(finalizado)
                                    {
                                        PruebaFragment.mainPagerObserver=this
                                        val myPagerCount=myViewPagerObserver?.paginaPrimera()
                                        if(myPagerCount==1)
                                        {
                                            //cuando solo hay una pagina mostrar las respuestas de forma manual de esa pagina
                                            mostrarRespuestas(0)
                                        }
                                    }
                                }

                                mAlert.show()
                            }
                        }
                    }
                }
                else//si es revision entonces solo terminar la actividad
                    this.activity?.finish()

            }
            else
            {
                myViewPagerObserver?.paginaSiguiente()
            }
        }

        myFrag.btnAnterior.setOnClickListener {
            myViewPagerObserver?.paginaAnterior()
        }

        return myFrag
    }

    private fun mostrarRespuestasCorrectas(preguntaId: Int?):ArrayList<Respuesta> {
        if(activity!=null)
        {
            return RespuestaDAO(activity!!.applicationContext).listarRespuestasCorrectas(preguntaId!!)
        }
        return arrayListOf()
    }

    private fun ultimaPregunta(actual: Int, final: Int): Boolean {
        return actual==final
    }

    private fun respuestas(preguntaId:Int?):ArrayList<Respuesta>
    {
        return RespuestaDAO(activity!!.applicationContext).ListarRespuestas(preguntaId!!)
    }

    private fun opcionRespuesta(opcionId:Int?, respuestas:ArrayList<Respuesta>, viewResp:LinearLayout)
    {
        when(opcionId)
        {
            1->
            {//multiple opciones
                for (item in respuestas)
                {
                    val myCheckBox = CheckBox(activity!!.applicationContext)
                    myCheckBox.text=item.descripcion?.toUpperCase(Locale.ROOT)
                    myCheckBox.id=item.id!!

                    myCheckBox.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    myCheckBox.setTextColor(Color.WHITE)
                    //myCheckBox.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.colorAccent,null))

                    myCheckBox.setOnCheckedChangeListener { checkButton, checked ->
                        val resp=respuestas.find { x->x.id==checkButton.id }

                        if(resp!=null)
                        {
                            if(checked)
                            {
                                respuestasSelected?.add(resp.id!!)
                                mostrarContiuar(respuestasSelected!!)
                            }
                            else
                            {
                                respuestasSelected?.removeAt(respuestasSelected!!.indexOf(resp.id))
                                mostrarContiuar(respuestasSelected!!)
                            }
                        }
                    }
                    viewResp.orientation=LinearLayout.VERTICAL
                    viewResp.addView(myCheckBox)
                }
            }
            2->
            {
                val myChipGroup=ChipGroup(activity!!.applicationContext)
                val params = ChipGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                myChipGroup.chipSpacingVertical=10
                myChipGroup.chipSpacingHorizontal=10
                myChipGroup.isSingleSelection=true
                myChipGroup.layoutParams=params

                for (item in respuestas)
                {
                    val myChipChoice= Chip(activity!!.applicationContext)

                    myChipChoice.gravity = (Gravity.CENTER)
                    myChipChoice.text=item.descripcion?.toUpperCase(Locale.ROOT)
                    myChipChoice.id= item.id!!
                    myChipChoice.isCheckable=true
                    myChipChoice.setPadding(30,30,30,30)

                    myChipChoice.setTextColor(Color.WHITE)
                    myChipChoice.setChipBackgroundColorResource(R.color.colorAccent)

                    myChipChoice.setOnCheckedChangeListener { buttonView, isChecked ->

                        val resp=respuestas.find { x->x.id==buttonView.id }

                        if(resp!=null)
                        {
                            if(isChecked)
                            {
                                respuestasSelected?.add(resp.id!!)
                                mostrarContiuar(respuestasSelected!!)
                            }
                            else
                            {
                                respuestasSelected?.removeAt(respuestasSelected!!.indexOf(resp.id))
                                mostrarContiuar(respuestasSelected!!)
                            }
                        }
                    }

                    myChipGroup.addView(myChipChoice)
                }
                viewResp.orientation=LinearLayout.VERTICAL
                viewResp.addView(myChipGroup)
            }
            3->
            {//
                if(respuestas.size>0)
                {
                    txtSinRespuesta?.visibility=View.GONE

                    for (letter in respuestas[0].descripcion.toString())
                    {
                            val myLetter=EditText(activity!!.applicationContext)
                            //myLetter.setText(item.descripcion?.toUpperCase())
                            myLetter.id= respuestas[0].id!!

                            myLetter.layoutParams=LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )

                        myLetter.isFocusable=false
                            //myLetter.isEnabled=false
                            myLetter.gravity=Gravity.CENTER
                            //myLetter.setTextColor(Color.TRANSPARENT)
                            myLetter.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.WHITE,BlendModeCompat.SRC_IN)

                        myLetter.setOnClickListener {
                            if(myLetter.text.isNotEmpty())
                            {
                               val mElement= letrasButton.find{ x-> !x.isEnabled && x.text==myLetter.text.toString() }

                                    if(mElement!=null)
                                    {
                                        mElement.setTextColor(Color.WHITE)
                                        mElement.isEnabled=true
                                        letrasEditText.find { x->x.text.toString()==myLetter.text.toString()}?.text?.clear()
                                    }
                                if(!letrasEditTextEstaLlena(letrasEditText))
                                    mostrarContiuar(View.GONE,btnContinuar)
                            }
                        }
                            val params = ActionBar.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            params.setMargins(15, 0, 15, 0)
                            myLetter.layoutParams = params

                            letrasEditText.add(myLetter)
                            viewResp.addView(myLetter)
                    }

                    val miAlfabeto=generarAlfabeto(respuestas[0].descripcion.toString())
                    val alfabetoAdapter=AlfabetoAdapter(activity!!.applicationContext,miAlfabeto.toUpperCase(
                        Locale.ROOT), letrasEditText,letrasButton, btnContinuar,respuestasSelected,myPpregunta.id)
                    myOpciones?.adapter=alfabetoAdapter
                }
                else
                {
                    txtSinRespuesta?.visibility=View.VISIBLE
                    mostrarContiuar(View.VISIBLE,btnContinuar)
                }
            }
        }
    }

    private fun generarAlfabeto(preAlfabeto: String):String {
        val randomValues = List(8) { Random.nextInt(65, 90).toChar() }
        return desordenar("$preAlfabeto${randomValues.joinToString(separator = "")}")
    }

    private fun desordenar(theWord: String):String {

        val theTempWord=theWord.toMutableList()

        for (item in 0..Random.nextInt(theTempWord.count()/2,theTempWord.count()-1))
        {
            val indexA=Random.nextInt(theTempWord.count()-1)
            val indexB=Random.nextInt(theTempWord.count()-1)

            val temp=theTempWord[indexA]

            theTempWord[indexA]=theTempWord[indexB]
            theTempWord[indexB]=temp
        }

        return theTempWord.joinToString(separator = "")
    }

    class AlfabetoAdapter(private var context: Context, private var alfabeto:String, private var letrasEditText:ArrayList<EditText>,
                          private var letrasButton:ArrayList<Button>, private var btnContinuar:Button?,
                          private var respuestasSelected:ArrayList<Int>?,private var preguntaId: Int?):BaseAdapter()
    {
        override fun getView(i: Int, view: View?, container: ViewGroup?): View? {

            var myView=view
            var myButton:Button?= null

            if(myView==null)
            {
                myView=LayoutInflater.from(context).inflate(R.layout.item_alfabeto,container,false)

                myButton= myView?.findViewById(R.id.opcion)
                myButton?.text= alfabeto[i].toString()

                myButton?.setOnClickListener {
                    if(!letrasEditTextEstaLlena(letrasEditText))
                    {
                        for (item in letrasEditText)
                        {
                            if(item.text.isEmpty())
                            {
                                item.setText(myButton.text.toString())
                                break
                            }
                        }

                        myButton.isEnabled=false
                        myButton.setTextColor(Color.TRANSPARENT)

                        if(letrasEditTextEstaLlena(letrasEditText))
                        {
                            val palabraRespuesta =StringBuilder()

                            letrasEditText.forEach{palabraRespuesta.append(it.text.toString())}

                            if(respuestas(preguntaId)[0].descripcion?.trim()==palabraRespuesta.toString())
                                respuestasSelected?.add(letrasEditText[0].id)
                            else
                                respuestasSelected?.add(-1)

                            mostrarContiuar(View.VISIBLE,btnContinuar)
                        }
                    }
                    else
                        mostrarContiuar(View.VISIBLE,btnContinuar)
                }
                if (myButton != null) {
                    letrasButton.add(myButton)
                }
            }

            return myView
        }

        override fun getItem(p0: Int): Any? {
            return null
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return alfabeto.count()
        }
        private fun respuestas(preguntaId:Int?):ArrayList<Respuesta>
        {
            return RespuestaDAO(context).ListarRespuestas(preguntaId!!)
        }
    }

    private fun mostrarContiuar(chechedList:ArrayList<Int>)
    {
        if(chechedList.count()>0)
        {
            btnContinuar?.visibility=View.VISIBLE
        }
        else
        {
            btnContinuar?.visibility=View.GONE
        }
    }

    override fun mostrarRespuestas(pos: Int) {
        //esto mismo se puede hacer con la variable "respuestas" del metodo opcion de respuestas para no hacer dos llamadas a la bd sino solo 1
        val pageRespuestas=mostrarRespuestasCorrectas(listPreguntas[pos].id)

        pageRespuestas.forEach {
            if(it.correcta!!)
            {
                val myTextView=TextView(context)
                myTextView.text=it.descripcion
                myTextView.setTextColor(Color.WHITE)

                val params = LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(16, 0, 0, 0)

                myTextView.layoutParams = params
                myTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_succes, 0, 0, 0)

                myTextView.gravity=Gravity.CENTER
                myTextView.textAlignment=TextView.TEXT_ALIGNMENT_CENTER

                myFrags?.get(pos)?.viewMostrarResp?.visibility=View.VISIBLE
                myFrags?.get(pos)?.viewMostrarResp?.addView(myTextView)
            }
        }
    }

    override fun estaEnRevision(): Boolean {
       return revisar
    }

    override fun onDestroy() {
        //al destruir la actividad hacer que las variables estaticas se reinicien
        quizRespuestas= arrayListOf()
        listPreguntas= arrayListOf()
        myFrags= arrayListOf()
        super.onDestroy()
    }

}
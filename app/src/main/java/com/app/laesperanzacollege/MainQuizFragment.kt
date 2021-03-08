package com.app.laesperanzacollege

import Observers.MainViewPagerObserver
import Observers.QuizesAdapterObserver
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
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.Utils.Companion.generarAlfabeto
import com.app.laesperanzacollege.Utils.Companion.letrasEditTextEstaLlena
import com.app.laesperanzacollege.Utils.Companion.mostrarContiuar
import com.app.laesperanzacollege.fragmentos.PruebaFragment
import com.app.laesperanzadao.RespuestaDAO
import com.app.laesperanzadao.ResultadoDAO
import com.app.laesperanzadao.UsuarioQuizzDAO
import com.app.laesperanzadao.enums.EstadoQuiz
import com.app.laesperanzadao.enums.OpcionesDeRespuesta
import com.app.laesperanzadao.enums.TipodeTest
import com.app.laesperanzaedm.model.*
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.alert_finalizarquiz.view.*
import kotlinx.android.synthetic.main.item_alfabeto.view.*
import kotlinx.android.synthetic.main.mainquizfragment.view.*
import java.text.DecimalFormat
import java.util.*


class MainQuizFragment(
    private var myPpregunta: Pregunta,
    private var actual: Int,
    private var final: Int,
    private var usuarioId: Int,
    private var usuarioQuizId: Int,
    private var mquiz: Quiz,
    private var mOpe: TipodeTest
)
    : Fragment(), MainViewPagerObserver
{
    private var btnContinuar:Button?=null
    private var respuestasSelected:ArrayList<Int>?= arrayListOf()
    private var letrasEditText:ArrayList<EditText> = arrayListOf()
    private var letrasButton:ArrayList<Button> = arrayListOf()
    private var txtSinRespuesta:TextView?=null
    private var viewMostrarRespuestas:LinearLayoutCompat?=null
    private var finalizar:Boolean=false
    private var finalizado:Boolean=false
    private var revisar:Boolean=false
    private var maxPuntajeDePregunta:Float=0.0f
    private var puntajeTotalPregunta:Float=0.0f
    private var multichoiceCorrectAnswerCounter:Int=0
    private var quiz:Quiz= mquiz
    private var puedePuntuarse:Int=0
    private var alfabet:RecyclerView?=null

    companion object
    {
        var myViewPagerObserver:ViewPagerObserver?=null
        var quizRespuestas:ArrayList<ResultadoQuiz>?= arrayListOf()
        var listPreguntas:ArrayList<Pregunta> = arrayListOf()
        var myFrags:ArrayList<View>?= arrayListOf()
        var mQuizesAdapterObserver:QuizesAdapterObserver?=null
        var mPuntaje:Float=0.0f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myFrag=inflater.inflate(R.layout.mainquizfragment,container,false)

        PruebaFragment.mainPagerObserver=this
        myFrag.pregunta.text=myPpregunta.descripcion
        listPreguntas.add(myPpregunta)
        maxPuntajeDePregunta=quiz.puntaje.toFloat()/final.toFloat()
        myFrags?.add(myFrag)

        alfabet=myFrag.alphabet

        myFrag.indicadorPreguntaActual.text=actual.toString()
        myFrag.indicadorPreguntaFinal.text=final.toString()
        txtSinRespuesta=myFrag.sinResp
        viewMostrarRespuestas=myFrag.viewMostrarResp

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

        opcionRespuesta(myPpregunta.opcionDeRespuestaId,respuestas(myPpregunta.id),myFrag.viewRespuestas,activity!!.applicationContext)

        myFrag.btnContinuar.setOnClickListener {
            //antes de pasar a la pagina siguiente o finalizar se debe guardar las selecciones de esta pagina
            val pageResultado=ResultadoQuiz()
            pageResultado.usuarioQuizId=usuarioQuizId
            pageResultado.preguntaId=myPpregunta.id
            pageResultado.respuestasId=respuestasSelected

            if(puedePuntuarse==0)
            pageResultado.puntaje=puntajeTotalPregunta
            else
                pageResultado.puntaje=0.0f

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
                                if(!finalizado && mOpe==TipodeTest.Prueba)
                                {
                                    quizRespuestas!!.forEach {
                                        ResultadoDAO(context!!).guardarResultados(it)
                                    }

                                    mQuizesAdapterObserver?.updateRecyClerQuiz()
                                }
                            }
                        }
                    }

                    finalizado=true

                    val mAlert= AlertDialog.Builder(context!!).create()
                    mAlert.setCancelable(false)
                    val mAlertView= LayoutInflater.from(context).inflate(R.layout.alert_finalizarquiz,null)

                    //asignar puntaje del test
                    mAlertView.findViewById<TextView>(R.id.puntaje).text= DecimalFormat("#.##").format(mPuntaje).toString()
                    mAlertView.findViewById<TextView>(R.id.txtValor).text= "Valor: ${quiz.puntaje} Pts"

                    mAlert.setView(mAlertView)

                    mAlertView.AlertTer.setOnClickListener {
                        mAlert.dismiss()
                        activity!!.finish()
                    }

                    mAlert.window?.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,android.R.color.transparent,null))

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

    private fun opcionRespuesta(
        opcionId: Int?,
        respuestas: ArrayList<Respuesta>,
        viewResp: LinearLayout,
        applicationContext: Context
    )
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

                    if(item.correcta!!)
                        multichoiceCorrectAnswerCounter++

                    myCheckBox.setTextColor(Color.WHITE)

                    myCheckBox.setOnCheckedChangeListener { checkButton, checked ->
                        val resp=respuestas.find { x->x.id==checkButton.id }

                        if(resp!=null)
                        {
                            if(checked)
                            {
                                respuestasSelected?.add(resp.id!!)
                                continuar(respuestasSelected!!)

                                if(resp.correcta!!) {
                                    puntuar(multichoiceCorrectAnswerCounter,1)
                                } else {
                                    puedePuntuarse++
                                }
                            }
                            else
                            {
                                respuestasSelected?.removeAt(respuestasSelected!!.indexOf(resp.id))
                                continuar(respuestasSelected!!)
                                if(resp.correcta!!)
                                    desPuntuar(multichoiceCorrectAnswerCounter,1)
                                else
                                    puedePuntuarse--
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
                                continuar(respuestasSelected!!)

                                if(resp.correcta!!)
                                puntuar(multichoiceCorrectAnswerCounter,2)
                                else {
                                    puedePuntuarse++
                                }
                            }
                            else
                            {
                                respuestasSelected?.removeAt(respuestasSelected!!.indexOf(resp.id))
                                continuar(respuestasSelected!!)

                                if(resp.correcta!!)
                                desPuntuar(multichoiceCorrectAnswerCounter,2)
                                else
                                    puedePuntuarse--
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

                    val flexLayoutAdaptative= FlexboxLayout(applicationContext)
                    flexLayoutAdaptative.flexWrap=FlexWrap.WRAP

                    for (letter in respuestas[0].descripcion.toString())
                    {
                            val myLetter=EditText(activity!!.applicationContext)
                            myLetter.id= respuestas[0].id!!

                            myLetter.layoutParams=LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )

                        myLetter.isFocusable=false
                            myLetter.gravity=Gravity.CENTER
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
                                {
                                    mostrarContiuar(View.GONE,btnContinuar)
                                    desPuntuar(-1,3)
                                }
                            }
                        }
                            val params = ActionBar.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            params.setMargins(15, 0, 15, 0)
                            myLetter.layoutParams = params

                        if(letrasEditText.size<=respuestas[0].descripcion!!.length)
                            letrasEditText.add(myLetter)

                        flexLayoutAdaptative.addView(myLetter)
                    }

                    viewResp.addView(flexLayoutAdaptative)

                    val miAlfabeto= generarAlfabeto(respuestas[0].descripcion.toString())

                    alfabet?.layoutManager=FlexboxLayoutManager(applicationContext)
                    alfabet?.adapter=AlfaAdapter1(miAlfabeto.toUpperCase(Locale.ROOT),
                        letrasEditText,myPpregunta.id)
                }
                else
                {
                    txtSinRespuesta?.visibility=View.VISIBLE
                    mostrarContiuar(View.VISIBLE,btnContinuar)
                }
            }
        }
    }

    private fun desPuntuar(multichoiceCorrectAnswerCounter: Int,opcionDeRespuesta: Int) {

        when(opcionDeRespuesta)
        {
            OpcionesDeRespuesta.OpcionMultiple.index-> {
                if(puntajeTotalPregunta>0)
                {
                    val puntuacion=maxPuntajeDePregunta/multichoiceCorrectAnswerCounter
                    mPuntaje-= puntuacion
                    puntajeTotalPregunta-= puntuacion
                }
            }
            OpcionesDeRespuesta.FalsoVerdadero.index-> {
                if(puntajeTotalPregunta>0)
                {
                    mPuntaje-=maxPuntajeDePregunta
                    puntajeTotalPregunta-= maxPuntajeDePregunta
                }
            }
            OpcionesDeRespuesta.CompletaPalabra.index-> {
                if(puntajeTotalPregunta>0)
                {
                    mPuntaje-=maxPuntajeDePregunta
                    puntajeTotalPregunta-= maxPuntajeDePregunta
                }
            }
        }
    }

    private fun puntuar(multichoiceCorrectAnswerCounter: Int,opcionDeRespuesta: Int) {
        when(opcionDeRespuesta)
        {
            OpcionesDeRespuesta.OpcionMultiple.index->
            {
                    val puntuacion=maxPuntajeDePregunta/multichoiceCorrectAnswerCounter
                    mPuntaje+= puntuacion
                    puntajeTotalPregunta+= puntuacion
            }
            OpcionesDeRespuesta.FalsoVerdadero.index->
            {
                    mPuntaje+=maxPuntajeDePregunta
                    puntajeTotalPregunta+= maxPuntajeDePregunta
            }
            OpcionesDeRespuesta.CompletaPalabra.index->
            {
                mPuntaje+=maxPuntajeDePregunta
                puntajeTotalPregunta+= maxPuntajeDePregunta
            }
        }
    }

    inner class AlfaAdapter1(private var alfabeto:String,private var letrasEditText:ArrayList<EditText>,var preguntaId: Int?): RecyclerView.Adapter<mViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
            val mView= LayoutInflater.from(parent.context).inflate(R.layout.item_alfabeto,parent,false)
            return mViewHolder(mView)
        }

        override fun getItemCount(): Int {
            return alfabeto.count()
        }

        override fun onBindViewHolder(holder: mViewHolder, position: Int) {
            holder.bindItem(alfabeto[position],letrasEditText,preguntaId)
        }
    }

  inner  class mViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mButton:Button=itemView.opcion

        fun bindItem(letter: Char, letrasEditText: ArrayList<EditText>, preguntaId: Int?) {
            mButton.text=letter.toString()
            letrasButton.add(mButton)

            mButton.setOnClickListener {
                if(!letrasEditTextEstaLlena(letrasEditText))
                {
                    for (item in letrasEditText)
                    {
                        if(item.text.isEmpty())
                        {
                            item.setText(mButton.text.toString())
                            break
                        }
                    }

                    mButton.isEnabled=false
                    mButton.setTextColor(Color.TRANSPARENT)

                    if(letrasEditTextEstaLlena(letrasEditText))
                    {
                        val palabraRespuesta =obtenerPalabraRespuesta(letrasEditText)

                        if(respuestas(preguntaId)[0].descripcion?.trim()?.toUpperCase(Locale.ROOT) == palabraRespuesta)
                        {
                            if(respuestasSelected?.isEmpty()!!)
                                respuestasSelected?.add(letrasEditText[0].id)

                            puntuar(-1,3)
                        }
                        else
                        {
                            if(respuestasSelected?.isNotEmpty()!!)
                                respuestasSelected?.remove(0)

                            desPuntuar(-1,3)
                        }

                        mostrarContiuar(View.VISIBLE,btnContinuar)
                    }
                    else
                        desPuntuar(-1,3)
                }
                else
                {
                    mostrarContiuar(View.VISIBLE,btnContinuar)
                    val palabraRespuesta =obtenerPalabraRespuesta(letrasEditText)

                    if(respuestas(preguntaId)[0].descripcion?.trim()?.toUpperCase(Locale.ROOT) == palabraRespuesta)
                    {
                        if(respuestasSelected?.isEmpty()!!)
                            respuestasSelected?.add(letrasEditText[0].id)

                        puntuar(-1,3)
                    }
                    else
                    {
                        if(respuestasSelected?.isNotEmpty()!!)
                            respuestasSelected?.remove(0)

                        desPuntuar(-1,3)
                    }
                }
            }

        }

      private fun obtenerPalabraRespuesta(letrasEditText: ArrayList<EditText>): String {
          val palabraRespuesta =StringBuilder()

          letrasEditText.forEach{
              palabraRespuesta.append(it.text.toString())
          }
          return palabraRespuesta.toString()
      }
    }

    private fun continuar(chechedList:ArrayList<Int>)
    {
        if(chechedList.count()>0)
            mostrarContiuar(View.VISIBLE,btnContinuar)
        else
            mostrarContiuar(View.GONE,btnContinuar)
    }

    override fun mostrarRespuestas(pos: Int) {
        //esto mismo se puede hacer con la variable "respuestas" del metodo opcion de respuestas para no hacer dos llamadas a la bd sino solo 1
        val pageRespuestas=mostrarRespuestasCorrectas(listPreguntas[pos].id)

        val childElements= myFrags?.get(pos)?.viewMostrarResp?.childCount

        if (childElements != null) {
            if(childElements<=1)
            {
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
        mPuntaje=0.0f
        super.onDestroy()
    }
}
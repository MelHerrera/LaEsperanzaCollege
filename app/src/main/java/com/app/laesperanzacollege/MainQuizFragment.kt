package com.app.laesperanzacollege

import Observers.ViewPagerObserver
import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import com.app.laesperanzacollege.Utils.Companion.letrasEditTextEstaLlena
import com.app.laesperanzadao.RespuestaDAO
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Respuesta
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.app.laesperanzacollege.Utils.Companion.mostrarContiuar
import kotlinx.android.synthetic.main.mainquizfragment.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class MainQuizFragment(private var myPpregunta:Pregunta,private var actual:Int,private var final:Int): Fragment()
{
    var myOpciones:GridView?=null
    var hasDrawableRight=false
    var btnContinuar:Button?=null
    var checkedSelected:ArrayList<Respuesta> = arrayListOf()
    var letrasEditText:ArrayList<EditText> = arrayListOf()
    var letrasButton:ArrayList<Button> = arrayListOf()
    var txtSinRespuesta:TextView?=null

    companion object
    {
        var myViewPagerObserver:ViewPagerObserver?=null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myFrag=inflater.inflate(R.layout.mainquizfragment,container,false)

        myFrag.pregunta.text=myPpregunta.descripcion

        myFrag.indicadorPreguntaActual.text=actual.toString()
        myFrag.indicadorPreguntaFinal.text=final.toString()
        txtSinRespuesta=myFrag.sinResp

        myOpciones=myFrag.opciones
        btnContinuar=myFrag.btnContinuar

        if(ultimaPregunta(actual,final))
        {
            myFrag.btnContinuar.text=context?.getString(R.string.txt_finaliza)
        }
        else
            myFrag.btnContinuar.text=context?.getString(R.string.txt_siguiente)

        //set this theme for use Chip and ChipGroup
        activity!!.applicationContext.setTheme(R.style.Theme_MaterialComponents)

        opcionRespuesta(myPpregunta.opcionDeRespuestaId,respuestas(myPpregunta.id),myFrag.viewRespuestas,inflater)

        myFrag.btnContinuar.setOnClickListener {
            myViewPagerObserver?.paginaSiguiente()
        }

        myFrag.btnAnterior.setOnClickListener {
            myViewPagerObserver?.paginaAnterior()
        }
        return myFrag
    }

    private fun ultimaPregunta(actual: Int, final: Int): Boolean {
        return actual==final
    }

    fun respuestas(preguntaId:Int?):ArrayList<Respuesta>
    {
        return RespuestaDAO(activity!!.applicationContext).ListarRespuestas(preguntaId!!)
    }

    fun opcionRespuesta(opcionId:Int?,respuestas:ArrayList<Respuesta>,viewResp:LinearLayout,inflater: LayoutInflater)
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

                    myCheckBox.setOnCheckedChangeListener { compoundButton, checked ->
                        val resp=respuestas.find { x->x.id==compoundButton.id }
                        if(resp!=null)
                        {
                            if(checked)
                            {
                                checkedSelected.add(resp)
                                mostrarContiuar(checkedSelected)
                            }
                            else
                            {
                                checkedSelected.remove(resp)
                                mostrarContiuar(checkedSelected)
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

                myChipGroup.setOnCheckedChangeListener { group, checkedId ->
                    if(checkedId!=-1)
                    {
                        val mRespuesta=respuestas.find { x->x.id==checkedId }

                        if(mRespuesta!=null)
                        {
                            mostrarContiuar(View.VISIBLE,btnContinuar)
                        }
                    }
                    else
                    {
                        mostrarContiuar(View.GONE,btnContinuar)
                    }
                }
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
                            //myLetter.id=letter.id!!

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
                        Locale.ROOT), letrasEditText,letrasButton, btnContinuar)
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
                          private var letrasButton:ArrayList<Button>, private var btnContinuar:Button?):BaseAdapter()
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
                            mostrarContiuar(View.VISIBLE,btnContinuar)
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
    }

    fun mostrarContiuar(chechedList:ArrayList<Respuesta>)
    {
        if(chechedList.isNotEmpty())
        {
            btnContinuar?.visibility=View.VISIBLE
        }
        else
        {
            btnContinuar?.visibility=View.GONE
        }
    }
}
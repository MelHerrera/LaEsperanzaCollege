package com.app.laesperanzacollege

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.app.laesperanzadao.RespuestaDAO
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Respuesta
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.mainquizfragment.view.*
import java.util.*
import kotlin.collections.ArrayList


class MainQuizFragment(private var myPpregunta:Pregunta,private var actual:Int,private var final:Int): Fragment()
{
    var myOpciones:GridView?=null
    var hasDrawableRight=false
    var btnContinuar:Button?=null
    var checkedSelected:ArrayList<Respuesta> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myFrag=inflater.inflate(R.layout.mainquizfragment,container,false)

        myFrag.pregunta.text=myPpregunta.descripcion
        myFrag.preguntaActual.text=actual.toString()
        myFrag.preguntaFinal.text=final.toString()

        myOpciones=myFrag.opciones
        btnContinuar=myFrag.btnContinuar
        //set this theme for use Chip and ChipGroup
        activity!!.applicationContext.setTheme(R.style.Theme_MaterialComponents)

        opcionRespuesta(myPpregunta.opcionDeRespuestaId,respuestas(myPpregunta.id),myFrag.viewRespuestas,inflater)

        return myFrag
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
            {
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
                    myCheckBox.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.colorAccent,null))

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
                myChipGroup.setPadding(10,10,10,10)

                myChipGroup.setOnCheckedChangeListener { group, checkedId ->
                    if(checkedId!=-1)
                    {
                        val mRespuesta=respuestas.find { x->x.id==checkedId }

                        if(mRespuesta!=null)
                        {
                            mostrarContiuar(View.VISIBLE)
                        }
                    }
                    else
                       mostrarContiuar(View.GONE)
                }
                for (item in respuestas)
                {
                    val myChipChoice= Chip(activity!!.applicationContext)

                    myChipChoice.gravity = (Gravity.CENTER_VERTICAL or Gravity.START)
                    myChipChoice.text=item.descripcion?.toUpperCase(Locale.ROOT)
                    myChipChoice.id= item.id!!
                    myChipChoice.isCheckable=true

                    myChipChoice.setTextColor(Color.WHITE)
                    myChipChoice.setChipBackgroundColorResource(R.color.colorAccent)

                    myChipGroup.addView(myChipChoice)
                }
                viewResp.orientation=LinearLayout.VERTICAL
                viewResp.addView(myChipGroup)
            }
            3->
            {
                for (item in respuestas)
                {
                    val myLetter=EditText(activity!!.applicationContext)
                    myLetter.setText(item.descripcion?.toUpperCase())
                    myLetter.id=item.id!!

                    myLetter.layoutParams=LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    myLetter.isEnabled=false
                    myLetter.gravity=Gravity.CENTER
                    myLetter.setTextColor(Color.TRANSPARENT)
                    myLetter.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.WHITE,BlendModeCompat.SRC_IN)

                    val params = ActionBar.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(5, 0, 3, 0)
                    myLetter.layoutParams = params

                    viewResp.addView(myLetter)

                    val alfabeto:MutableList<Char> = mutableListOf()
                    var x:Char='A'

                    while (x<='Z')
                    {
                        alfabeto.add(x)
                        x++
                    }

                    val alfabetoAdapter=AlfabetoAdapter(activity!!.applicationContext,alfabeto)
                    myOpciones?.adapter=alfabetoAdapter
                }
            }
        }
    }

    class AlfabetoAdapter(private var context: Context,private var alfabeto:MutableList<Char>):BaseAdapter()
    {
        override fun getView(i: Int, view: View?, container: ViewGroup?): View? {

            var myView=view

            if(myView==null)
            {
                myView=LayoutInflater.from(context).inflate(R.layout.item_alfabeto,container,false)
            }

           val myButton= myView?.findViewById<Button>(R.id.opcion)
            myButton?.text= alfabeto[i].toString()
            myButton?.setOnClickListener {
                Toast.makeText(context,"Selected ${myButton.text}",Toast.LENGTH_LONG).show()
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
            return alfabeto.size
        }
    }

   /* fun getRespuesta(respId:Int,listResp:ArrayList<Respuesta>,myButton: Button)
    {
        val respuesta=listResp.find { x->x.id==respId }

        if(!hasDrawableRight)
        {
            hasDrawableRight=true

            if(respuesta!=null)
            {
                if(respuesta.correcta!!)
                {
                    myButton.setRigthDrawable(R.drawable.ic_check_circle)
                }
                else
                {
                    myButton.setRigthDrawable(R.drawable.ic_clear)
                }
            }
        }
    }

    fun Button.setRigthDrawable(rigthDrawable:Int)
    {
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0,rigthDrawable, 0)
    }*/

    fun mostrarContiuar(visibility:Int)
    {
        btnContinuar?.visibility=visibility
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
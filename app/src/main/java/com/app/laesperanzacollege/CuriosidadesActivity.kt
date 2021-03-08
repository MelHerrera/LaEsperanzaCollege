package com.app.laesperanzacollege

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.laesperanzacollege.adaptadores.Cur
import com.app.laesperanzacollege.adaptadores.CurAdapter
import kotlinx.android.synthetic.main.activity_curiosidades.*

class CuriosidadesActivity : AppCompatActivity() {

    private var adapter: CurAdapter? = null
    private var curList:ArrayList<Cur>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curiosidades)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Curiosidades"


        curList = ArrayList<Cur>()
        layoutManager = LinearLayoutManager(this)
        adapter = CurAdapter(curList!!, this)
        //agregar forma al reclycer view
        curiosidadR.layoutManager = layoutManager
        curiosidadR.adapter=adapter

        var namelist:Array <String> = arrayOf(
            "Matematicas",
            "Matematicas",
            "Historia",
            "Historia",
            "Biologia",
            "Random"
        )

        val fotolist:Array <Int> = arrayOf(
            R.drawable.mat1,
            R.drawable.mat2,
            R.drawable.his1,
            R.drawable.his2,
            R.drawable.bi1,
            R.drawable.ran1
        )

        val textolist:Array<String> = arrayOf(
            "El signo «=», que sirve para indicar la igualdad e introducir el resultado de algún procedimiento o ecuación, fue creado por el médico y matemático inglés Robert Recorde, quien aseguró tras la invención que «dos cosas no pueden ser más iguales que dos rectas paralelas».",
            "Durante el siglo XVI, la multiplicación era considerada la operación matemática más difícil y compleja de todas. Por este motivo, solo los alumnos universitarios adquirían dicho conocimiento.",
            "A lo largo de numerosos siglos, incluso en la Edad Media, el baño fue una práctica habitual, sobre todo entre gente procedente de las altas clases sociales.",
            "Un Gnomon su ideas es observar la altura del Sol en el mediodía verdadero durante el paso de los días deduciéndola mediante el cambio en la longitud de la sombra que proyecta el\n" +
                    "estilo.",
            "El ornitorrinco representa una verdadera rareza viviente pues a pesar de ser un mamífero, ponen huevos",
            "En tu cuerpo hay 10 veces más bacterias que células. Pero tranquilo, la mayoría son buenas para tu organismo."

        )

        for (i in 0..namelist.size-1){
            val names = Cur(namelist[i],fotolist[i],textolist[i])
            names.name = namelist[i]
            names.foto = fotolist[i]
            names.texto = textolist[i]
            curList?.add(names)
        }
        adapter!!.notifyDataSetChanged()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
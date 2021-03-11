package com.app.laesperanzacollege

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_agregar_quiz.*
import kotlinx.android.synthetic.main.activity_formulas.*

class FormulasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulas)

        txtNoData.text=getString(R.string.sin_datos,getString(R.string.txt_formulas))
    }
}
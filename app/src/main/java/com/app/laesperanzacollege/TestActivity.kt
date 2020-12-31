package com.app.laesperanzacollege

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.app.laesperanzacollege.fragmentos.PruebaFragment
import com.app.laesperanzaedm.model.Quiz
import kotlin.math.log

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val quiz=intent.extras?.get(getString(R.string.keyNameUser)) as Quiz?

        supportActionBar?.elevation=0.0f
        supportActionBar?.title=""
        if(quiz!=null)
        {
            val myPruebaFragment=PruebaFragment()
            val myData=Bundle()
            myData.putSerializable(getString(R.string.keyNameUser),quiz)

            myPruebaFragment.arguments=myData
            supportFragmentManager.beginTransaction().add(R.id.content,myPruebaFragment,null).commit()
        }
    }
}
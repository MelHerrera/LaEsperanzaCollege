package com.app.laesperanzacollege

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.app.laesperanzadao.enums.OperacionesCrud
import kotlinx.android.synthetic.main.activity_actividades.*
import kotlinx.android.synthetic.main.activity_unidades.*

class ActividadesActivity : AppCompatActivity() {
    var myTool: Toolbar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title=getString(R.string.txtActividades)

        floatingAgregarActividad.setOnClickListener {
            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE,"")
                .putExtra(CalendarContract.Events.EVENT_LOCATION,"Aqui")
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,System.currentTimeMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,System.currentTimeMillis()+(60*60*1000))

            startActivity(intent)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
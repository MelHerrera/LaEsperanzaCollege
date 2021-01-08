package com.app.laesperanzadao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.app.laesperanzaedm.database.MyDbAdapter
import com.app.laesperanzaedm.database.ResultadoContract
import com.app.laesperanzaedm.database.UsuarioQuizzContract
import com.app.laesperanzaedm.model.ResultadoQuiz

class ResultadoDAO(context: Context) {
    private var mySqlDatabase: SQLiteDatabase?=null
    private var myDbAdapter: MyDbAdapter?=null

    init {
        myDbAdapter= MyDbAdapter(context)
        mySqlDatabase= myDbAdapter!!.openDatabase()
    }

    fun guardarResultados(resultado: ResultadoQuiz):Boolean
    {
        val datos= ContentValues()
        var result:Long?=null

        datos.put(ResultadoContract.COLUMN_PREGUNTAID,resultado.preguntaId)
        datos.put(ResultadoContract.COLUMN_RESPUESTASID,resultado.respuestasId.toString())
        datos.put(ResultadoContract.COLUMN_USUARIOQUIZID,resultado.usuarioQuizId)

        result=mySqlDatabase?.insert(ResultadoContract.TABLE_NAME,null,datos)

        return result!=(-1).toLong()
    }
}
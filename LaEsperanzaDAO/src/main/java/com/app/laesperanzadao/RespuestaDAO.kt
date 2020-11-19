package com.app.laesperanzadao

import android.content.ClipDescription
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.app.laesperanzaedm.database.MyDbAdapter
import com.app.laesperanzaedm.database.PreguntaContract
import com.app.laesperanzaedm.database.QuizContract
import com.app.laesperanzaedm.database.RespuestaContract
import com.app.laesperanzaedm.model.Quiz
import com.app.laesperanzaedm.model.Respuesta

class RespuestaDAO(context: Context) {
    var mySqlDatabase: SQLiteDatabase?=null
    var myDbAdapter: MyDbAdapter?=null
    var result:Long?=null

    init {
        myDbAdapter= MyDbAdapter(context)
        mySqlDatabase= myDbAdapter!!.openDatabase()
    }

    fun Insertar(respuesta: Respuesta):Boolean
    {
        var datos= ContentValues()
        var correcta:Int=0
        datos.put(RespuestaContract.COLUMN_DESCRIPCION,respuesta.descripcion)
        if(respuesta.correcta!!)
        {
            correcta=1
        }
        datos.put(RespuestaContract.COLUMN_CORRECTA,correcta)
        datos.put(RespuestaContract.COLUMN_PREGUNTAID,respuesta.preguntaId)

        result=mySqlDatabase?.insert(RespuestaContract.TABLE_NAME,null,datos)

        return result!=(-1).toLong()
    }

    fun ListarRespuestas():ArrayList<Respuesta>
    {
        var myRespuesta: Respuesta?=null
        var myListRespuesta:ArrayList<Respuesta> = arrayListOf()

        var res=mySqlDatabase?.query(RespuestaContract.TABLE_NAME,null,null,null,null,null,null,null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myRespuesta= Respuesta()
                myRespuesta.id=res.getInt(res.getColumnIndex(RespuestaContract.COLUMN_ID))
                myRespuesta.descripcion=res.getString(res.getColumnIndex(PreguntaContract.COLUMN_DESCRIPCION))
                myRespuesta.preguntaId=res.getInt(res.getColumnIndex(RespuestaContract.COLUMN_PREGUNTAID))
                myRespuesta.correcta= res.getInt(res.getColumnIndex(RespuestaContract.COLUMN_CORRECTA))==1

                myListRespuesta.add(myRespuesta)

                res.moveToNext()
            }
        }
        res.close()
        return myListRespuesta
    }

    fun ListarRespuestas(preguntaId:Int):ArrayList<Respuesta>
    {
        var myRespuesta: Respuesta?=null
        val myListRespuesta:ArrayList<Respuesta> = arrayListOf()
        val where="${RespuestaContract.COLUMN_PREGUNTAID}=?"

        val res=mySqlDatabase?.query(RespuestaContract.TABLE_NAME,null,where,
            arrayOf(preguntaId.toString()),null,null,null,null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myRespuesta= Respuesta()
                myRespuesta.id=res.getInt(res.getColumnIndex(RespuestaContract.COLUMN_ID))
                myRespuesta.descripcion=res.getString(res.getColumnIndex(PreguntaContract.COLUMN_DESCRIPCION))
                myRespuesta.preguntaId=res.getInt(res.getColumnIndex(RespuestaContract.COLUMN_PREGUNTAID))
                myRespuesta.correcta= res.getInt(res.getColumnIndex(RespuestaContract.COLUMN_CORRECTA))==1

                myListRespuesta.add(myRespuesta)

                res.moveToNext()
            }
        }
        res.close()
        return myListRespuesta
    }

    fun BuscarRespuesta(descripcion: String): Respuesta?
    {
        var miRespuesta = Respuesta()
        var selection="${RespuestaContract.COLUMN_DESCRIPCION}=?"
        var myCursor: Cursor?=mySqlDatabase?.query(RespuestaContract.TABLE_NAME,null,selection, arrayOf(descripcion),null,null,null)

        if(myCursor?.count!! >0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                miRespuesta.descripcion =myCursor.getString(myCursor.getColumnIndex(RespuestaContract.COLUMN_DESCRIPCION))
                miRespuesta.id=myCursor.getInt(myCursor.getColumnIndex(RespuestaContract.COLUMN_ID))
                miRespuesta.preguntaId=myCursor.getInt(myCursor.getColumnIndex(RespuestaContract.COLUMN_PREGUNTAID))
                miRespuesta.correcta=myCursor.getInt(myCursor.getColumnIndex(RespuestaContract.COLUMN_CORRECTA))==1

                myCursor.moveToNext()
            }

            return miRespuesta
        }
        return null
    }
}
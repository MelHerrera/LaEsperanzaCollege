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
        val datos= ContentValues()
        var correcta=0

        datos.put(RespuestaContract.COLUMN_ID,respuesta.id)
        datos.put(RespuestaContract.COLUMN_DESCRIPCION,respuesta.descripcion)
        datos.put(RespuestaContract.COLUMN_PREGUNTAID,respuesta.preguntaId)

        if(respuesta.correcta!!)
        {
            correcta=1
        }
        else
            correcta=0

        datos.put(RespuestaContract.COLUMN_CORRECTA,correcta)

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

    fun listarRespuestasCorrectas(preguntaId:Int):ArrayList<Respuesta>
    {
        var myRespuesta: Respuesta?=null
        val myListRespuesta:ArrayList<Respuesta> = arrayListOf()
        val where="${RespuestaContract.COLUMN_PREGUNTAID}=? AND ${RespuestaContract.COLUMN_CORRECTA}=?"

        val res=mySqlDatabase?.query(RespuestaContract.TABLE_NAME,null,where,
            arrayOf(preguntaId.toString(),1.toString()),null,null,null,null)

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
        val miRespuesta = Respuesta()
        val selection="${RespuestaContract.COLUMN_DESCRIPCION}=?"
        val myCursor: Cursor?=mySqlDatabase?.query(RespuestaContract.TABLE_NAME,null,selection, arrayOf(descripcion),null,null,null)

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

    fun Eliminar(PreguntaId: Int?):Boolean
    {
        val result=mySqlDatabase?.delete(RespuestaContract.TABLE_NAME,"${RespuestaContract.COLUMN_PREGUNTAID}=?", arrayOf(PreguntaId.toString()))

        if (result != null) {
            return result>0
        }
        return false
    }
}
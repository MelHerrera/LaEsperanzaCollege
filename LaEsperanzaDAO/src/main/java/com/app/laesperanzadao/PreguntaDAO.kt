package com.app.laesperanzadao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.app.laesperanzaedm.database.MyDbAdapter
import com.app.laesperanzaedm.database.PreguntaContract
import com.app.laesperanzaedm.database.QuizContract
import com.app.laesperanzaedm.database.UsuarioContract
import com.app.laesperanzaedm.model.Pregunta
import com.app.laesperanzaedm.model.Quiz

class PreguntaDAO(context: Context) {
    var mySqlDatabase: SQLiteDatabase?=null
    var myDbAdapter: MyDbAdapter?=null
    var result:Long?=null

    init {
        myDbAdapter= MyDbAdapter(context)
        mySqlDatabase= myDbAdapter!!.openDatabase()
    }

    fun Insertar(pregunta: Pregunta):Boolean
    {
        val datos= ContentValues()
        datos.put(PreguntaContract.COLUMN_DESCRIPCION,pregunta.descripcion)
        datos.put(PreguntaContract.COLUMN_OPCIONDERESPUESTAID,pregunta.opcionDeRespuestaId)
        datos.put(PreguntaContract.COLUMN_QUIZZID,pregunta.quizzId)

        result=mySqlDatabase?.insert(PreguntaContract.TABLE_NAME,null,datos)

        return result!=(-1).toLong()
    }

    fun actualizar(myPregunta: Pregunta):Boolean
    {
       /* var datos= ContentValues()
        datos.put(UnidadContract.COLUMN_NUMUNIDAD,myPregunta.descripcion)
        datos.put(UnidadContract.COLUMN_DESCRIPCION,myUnidad.descripcion)
        datos.put(UnidadContract.COLUMN_CODGRADO,myUnidad.codGrado)

        var query="${UnidadContract.COLUMN_NUMUNIDAD}=?"

        var res=mySqlDatabase?.update(
            UnidadContract.TABLE_NAME,
            datos,
            query,
            arrayOf(myUnidad.numUnidad.toString())
        )

        if (res != null) {
            return res>0
        }*/
        return false
    }

    fun ListarPreguntas():ArrayList<Pregunta>
    {
        var myPregunta: Pregunta?=null
        val myListPreguntas:ArrayList<Pregunta> = arrayListOf()

        val res=mySqlDatabase?.query(PreguntaContract.TABLE_NAME,null,null,null,null,null,null,null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myPregunta= Pregunta()
                myPregunta.id=res.getInt(res.getColumnIndex(PreguntaContract.COLUMN_ID))
                myPregunta.descripcion=res.getString(res.getColumnIndex(PreguntaContract.COLUMN_DESCRIPCION))
                myPregunta.opcionDeRespuestaId=res.getInt(res.getColumnIndex(PreguntaContract.COLUMN_OPCIONDERESPUESTAID))
                myPregunta.quizzId=res.getInt(res.getColumnIndex(PreguntaContract.COLUMN_QUIZZID))

                myListPreguntas.add(myPregunta)

                res.moveToNext()
            }
        }
        res.close()
        return myListPreguntas
    }


    fun ListarPreguntas(quizzId:Int):ArrayList<Pregunta>
    {
        var myPregunta: Pregunta?=null
        val myListPreguntas:ArrayList<Pregunta> = arrayListOf()
        val mySelection="${PreguntaContract.COLUMN_QUIZZID}=?"
        val res=mySqlDatabase?.query(PreguntaContract.TABLE_NAME,null,mySelection,
            arrayOf(quizzId.toString()),null,null,null,null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myPregunta= Pregunta()
                myPregunta.id=res.getInt(res.getColumnIndex(PreguntaContract.COLUMN_ID))
                myPregunta.descripcion=res.getString(res.getColumnIndex(PreguntaContract.COLUMN_DESCRIPCION))
                myPregunta.opcionDeRespuestaId=res.getInt(res.getColumnIndex(PreguntaContract.COLUMN_OPCIONDERESPUESTAID))
                myPregunta.quizzId=res.getInt(res.getColumnIndex(PreguntaContract.COLUMN_QUIZZID))

                myListPreguntas.add(myPregunta)

                res.moveToNext()
            }
        }
        res.close()
        return myListPreguntas
    }

    fun BuscarPregunta(descripcion:String): Pregunta?
    {
        val miPregunta = Pregunta()

        val query="${PreguntaContract.COLUMN_DESCRIPCION}=?"
        val myCursor: Cursor?=mySqlDatabase?.query(PreguntaContract.TABLE_NAME,null,query, arrayOf(descripcion),null,null,null)

        if(myCursor?.count!! >0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                miPregunta.id =myCursor.getInt(myCursor.getColumnIndex(PreguntaContract.COLUMN_ID))
                miPregunta.descripcion=myCursor.getString(myCursor.getColumnIndex(PreguntaContract.COLUMN_DESCRIPCION))
                miPregunta.quizzId=myCursor.getInt(myCursor.getColumnIndex(PreguntaContract.COLUMN_QUIZZID))
                miPregunta.opcionDeRespuestaId=myCursor.getInt(myCursor.getColumnIndex(PreguntaContract.COLUMN_OPCIONDERESPUESTAID))

                myCursor.moveToNext()
            }

            return miPregunta
        }
        return null
    }

    fun Eliminar(PreguntaId: Int?):Boolean
    {
        val result=mySqlDatabase?.delete(PreguntaContract.TABLE_NAME,"${PreguntaContract.COLUMN_ID}=?", arrayOf(PreguntaId.toString()))

        if (result != null) {
            return result>0
        }
        return false
    }
}
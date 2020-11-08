package com.app.laesperanzadao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.app.laesperanzaedm.database.MyDbAdapter
import com.app.laesperanzaedm.database.UnidadContract
import com.app.laesperanzaedm.database.UsuarioContract
import com.app.laesperanzaedm.model.Unidad
import com.app.laesperanzaedm.model.Usuario

class UnidadDAO(context: Context){
    var mySqlDatabase: SQLiteDatabase?=null
    var myDbAdapter: MyDbAdapter?=null
    var result:Long?=null

    init {
        myDbAdapter= MyDbAdapter(context)
        mySqlDatabase= myDbAdapter!!.openDatabase()
    }

    fun Insertar(unidad: Unidad):Boolean
    {
        var datos= ContentValues()
        datos.put(UnidadContract.COLUMN_NUMUNIDAD,unidad.numUnidad)
        datos.put(UnidadContract.COLUMN_DESCRIPCION,unidad.descripcion)
        datos.put(UnidadContract.COLUMN_CODGRADO,unidad.codGrado)

        var query="${UnidadContract.COLUMN_NUMUNIDAD}=?"
        var unidadConsulta=mySqlDatabase?.query(UnidadContract.TABLE_NAME,null,query, arrayOf(unidad.numUnidad.toString()),null,null,null)

        if(unidadConsulta?.count!! ==0)
        {
            result=mySqlDatabase?.insert(UnidadContract.TABLE_NAME,null,datos)
        }

        if(result!=null)
        {
            return result!=(-1).toLong()
        }
        return false
    }

    fun listarUnidades():ArrayList<Unidad>
    {
        var myListUnidades:ArrayList<Unidad> = ArrayList()
        var myUnidad: Unidad?= null

        var myCursor=mySqlDatabase?.query(UnidadContract.TABLE_NAME,null,null,null,null,null,null)

        if(myCursor?.count!! >0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                myUnidad= Unidad()
                myUnidad.numUnidad=myCursor.getInt(myCursor.getColumnIndex(UnidadContract.COLUMN_NUMUNIDAD))
                myUnidad.codGrado=myCursor.getString(myCursor.getColumnIndex(UnidadContract.COLUMN_CODGRADO))
                myUnidad.descripcion=myCursor.getString(myCursor.getColumnIndex(UnidadContract.COLUMN_DESCRIPCION))

                myListUnidades.add(myUnidad)

                myCursor.moveToNext()
            }

            return myListUnidades
        }
        return arrayListOf()
    }

    fun EliminarUnidades(numUnidades:ArrayList<String>):Boolean
    {
        var query=UnidadContract.COLUMN_NUMUNIDAD+" IN("

        for (item in numUnidades)
        {
            query+="$item,"
        }

        query=query.substring(0 until query.count()-1)

        query+=")"

        var queryfinal=query

        var result=mySqlDatabase?.delete(UnidadContract.TABLE_NAME,query,null)

        if (result != null) {
            return result>0
        }

        return false
    }

    fun actualizar(myUnidad:Unidad):Boolean
    {
        var datos= ContentValues()
        datos.put(UnidadContract.COLUMN_NUMUNIDAD,myUnidad.numUnidad)
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
        }
        return false
    }
}
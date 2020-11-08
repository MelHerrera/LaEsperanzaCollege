package com.app.laesperanzadao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.app.laesperanzaedm.database.MyDbAdapter
import com.app.laesperanzaedm.database.OpcionDeRespuestaContract
import com.app.laesperanzaedm.database.UsuarioContract
import com.app.laesperanzaedm.model.OpcionDeRespuesta
import com.app.laesperanzaedm.model.Usuario

class OpcionesDeRespuestaDAO(context: Context) {
    var mySqlDatabase: SQLiteDatabase?=null
    var myDbAdapter: MyDbAdapter?=null

    init {
        myDbAdapter= MyDbAdapter(context)
        mySqlDatabase= myDbAdapter!!.openDatabase()
    }

    fun listarOpciones():ArrayList<OpcionDeRespuesta>
    {
        var myListOpcionesRespuesta:ArrayList<OpcionDeRespuesta> = ArrayList()
        var myOpcionesDeRespuesta: OpcionDeRespuesta?= null

        var myCursor=mySqlDatabase?.query(OpcionDeRespuestaContract.TABLE_NAME,null,null,null,null,null,null)

        if(myCursor?.count!! >0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                myOpcionesDeRespuesta= OpcionDeRespuesta()
                myOpcionesDeRespuesta.id=myCursor.getInt(myCursor.getColumnIndex(OpcionDeRespuestaContract.COLUMN_ID))
                myOpcionesDeRespuesta.descripcion=myCursor.getString(myCursor.getColumnIndex(OpcionDeRespuestaContract.COLUMN_DESCRIPCION))

                myListOpcionesRespuesta.add(myOpcionesDeRespuesta)

                myCursor.moveToNext()
            }

            return myListOpcionesRespuesta
        }
        return arrayListOf()
    }

}
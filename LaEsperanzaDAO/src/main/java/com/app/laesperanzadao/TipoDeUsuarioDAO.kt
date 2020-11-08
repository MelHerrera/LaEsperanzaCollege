package com.app.laesperanzadao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.app.laesperanzaedm.database.MyDbAdapter
import com.app.laesperanzaedm.database.TipoDeUsuarioContract
import com.app.laesperanzaedm.database.UsuarioContract
import com.app.laesperanzaedm.model.Usuario

class TipoDeUsuarioDAO(context: Context){
    var mySqlDatabase:SQLiteDatabase?=null
    var myDbAdapter:MyDbAdapter?=null

    init {
     myDbAdapter= MyDbAdapter(context)
        mySqlDatabase= myDbAdapter!!.openDatabase()
    }

    fun obtenerTipoDeUsuario(tipoDeUsuarioId:Int?):String
    {
        var tipoDeUsuario:String=""
        var query=TipoDeUsuarioContract.COLUMN_ID+"=?"
        var myCursor: Cursor?=mySqlDatabase?.query(TipoDeUsuarioContract.TABLE_NAME,null,query, arrayOf(tipoDeUsuarioId.toString()),null,null,null)

        if(myCursor?.count!!>0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                tipoDeUsuario=myCursor.getString(myCursor.getColumnIndex(TipoDeUsuarioContract.COLUMN_DESCRIPCION))
                myCursor.moveToNext()
            }
        }
        mySqlDatabase?.close()
        myDbAdapter?.close()

        return tipoDeUsuario
    }
}
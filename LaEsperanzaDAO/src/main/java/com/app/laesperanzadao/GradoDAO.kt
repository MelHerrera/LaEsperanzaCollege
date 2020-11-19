package com.app.laesperanzadao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.app.laesperanzaedm.database.GradoContract
import com.app.laesperanzaedm.database.MyDbAdapter
import com.app.laesperanzaedm.database.TipoDeUsuarioContract
import com.app.laesperanzaedm.database.UsuarioContract
import com.app.laesperanzaedm.model.Grado
import com.app.laesperanzaedm.model.Usuario

class GradoDAO(context: Context) {
    var mySqlDatabase: SQLiteDatabase?=null
    var myDbAdapter: MyDbAdapter?=null

    init {
        myDbAdapter= MyDbAdapter(context)
        mySqlDatabase= myDbAdapter!!.openDatabase()
    }

    fun listarGrados():ArrayList<Grado>
    {
        var myListGrado:ArrayList<Grado> = ArrayList()
        var myGrado:Grado?= null
        var myCursor=mySqlDatabase?.query(GradoContract.TABLE_NAME,null,null,null,null,null,null)

        if(myCursor?.count!! >0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                myGrado= Grado()
                myGrado.codGrado=myCursor.getString(myCursor.getColumnIndex(GradoContract.COLUMN_CODGRADO))
                myGrado.descripcion=myCursor.getString(myCursor.getColumnIndex(GradoContract.COLUMN_DESCRIPCION))

                myListGrado.add(myGrado)

                myCursor.moveToNext()
            }
            myCursor.close()
            mySqlDatabase?.close()
            return myListGrado
        }
        return arrayListOf()
    }

    fun Buscar(codGrado:String):String
    {
        var grado:String=""
        var query="${GradoContract.COLUMN_CODGRADO}=?"
        var myCursor: Cursor?=mySqlDatabase?.query(GradoContract.TABLE_NAME,
            arrayOf(GradoContract.COLUMN_DESCRIPCION),query, arrayOf(codGrado),null,null,null)

        if(myCursor?.count!! >0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                grado=myCursor.getString(myCursor.getColumnIndex(GradoContract.COLUMN_DESCRIPCION))

                myCursor.moveToNext()
            }

            mySqlDatabase?.close()
            myDbAdapter?.close()

            return grado
        }
        return ""
    }

    fun buscarGrado(descripcionGrado:String):String
    {
        var grado:String=""
        var query="${GradoContract.COLUMN_DESCRIPCION}=?"

        var myCursor: Cursor?=mySqlDatabase?.query(GradoContract.TABLE_NAME,
            arrayOf(GradoContract.COLUMN_CODGRADO),query, arrayOf(descripcionGrado),null,null,null)

        if(myCursor?.count!! >0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                grado=myCursor.getString(myCursor.getColumnIndex(GradoContract.COLUMN_CODGRADO))

                myCursor.moveToNext()
            }

            mySqlDatabase?.close()
            myDbAdapter?.close()

            return grado
        }
        return ""
    }
}
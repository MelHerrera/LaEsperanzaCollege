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

class UsuarioDAO(context: Context) {
    var mySqlDatabase: SQLiteDatabase?=null
    var myDbAdapter: MyDbAdapter?=null
    var result:Long?=null

    init {
        myDbAdapter= MyDbAdapter(context)
        mySqlDatabase= myDbAdapter!!.openDatabase()
    }

    fun Eliminar(usuarioId: Int?):Boolean
    {
        var result=mySqlDatabase?.delete(UsuarioContract.TABLE_NAME,UsuarioContract.COLUMN_ID+"=?", arrayOf(usuarioId.toString()))

        if (result != null) {
            return result>0
        }
        return false
    }

    fun insertar(usuario:Usuario):Boolean
    {
        var datos= ContentValues()
        datos.put(UsuarioContract.COLUMN_NOMBRE,usuario.nombre)
        datos.put(UsuarioContract.COLUMN_APELLIDO,usuario.apellido)
        datos.put(UsuarioContract.COLUMN_USUARIO,usuario.usuario)
        datos.put(UsuarioContract.COLUMN_CONTRASE,usuario.contrase)
        datos.put(UsuarioContract.COLUMN_CODGRADO,usuario.codGrado)
        datos.put(UsuarioContract.COLUMN_TIPODEUSUARIOID,usuario.tipoDeUsuarioId)

        result=mySqlDatabase?.insert(UsuarioContract.TABLE_NAME,null,datos)

        return result!=(-1).toLong()
    }

    fun actualizar(usuario:Usuario):Boolean
    {
        var datos= ContentValues()
        datos.put(UsuarioContract.COLUMN_NOMBRE,usuario.nombre)
        datos.put(UsuarioContract.COLUMN_APELLIDO,usuario.apellido)
        datos.put(UsuarioContract.COLUMN_USUARIO,usuario.usuario)
        datos.put(UsuarioContract.COLUMN_CODGRADO,usuario.codGrado)

        var query="${UsuarioContract.COLUMN_ID}=?"

        var res=mySqlDatabase?.update(
            UsuarioContract.TABLE_NAME,
            datos,
            query,
            arrayOf(usuario.id.toString())
        )

        if (res != null) {
            return res>0
        }
        return false
    }

    fun Buscar(usuario:String, contrase:String):Usuario?
    {
        var miUsuario:Usuario= Usuario()
        var query=UsuarioContract.COLUMN_USUARIO+"=?"+ " and "+UsuarioContract.COLUMN_CONTRASE+"=?"
        var myCursor:Cursor?=mySqlDatabase?.query(UsuarioContract.TABLE_NAME,null,query, arrayOf(usuario,contrase),null,null,null)

        if(myCursor?.count!! >0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                miUsuario.id =myCursor.getInt(myCursor.getColumnIndex(UsuarioContract.COLUMN_ID))
                miUsuario.nombre=myCursor.getString(myCursor.getColumnIndex(UsuarioContract.COLUMN_NOMBRE))
                miUsuario.apellido=myCursor.getString(myCursor.getColumnIndex(UsuarioContract.COLUMN_APELLIDO))
                miUsuario.usuario=myCursor.getString(myCursor.getColumnIndex(UsuarioContract.COLUMN_USUARIO))
                miUsuario.codGrado=myCursor.getString(myCursor.getColumnIndex(UsuarioContract.COLUMN_CODGRADO))
                miUsuario.tipoDeUsuarioId=myCursor.getInt(myCursor.getColumnIndex(UsuarioContract.COLUMN_TIPODEUSUARIOID))

                myCursor.moveToNext()
            }

            return miUsuario
        }
        return null
    }

    fun listarEstudiantes():ArrayList<Usuario>
    {
        var myListEstu:ArrayList<Usuario> = ArrayList()
        var myEstu: Usuario?= null
        var myQuery=UsuarioContract.COLUMN_TIPODEUSUARIOID+"=?"
        var myCursor=mySqlDatabase?.query(UsuarioContract.TABLE_NAME,null,myQuery, arrayOf("2"),null,null,null)

        if(myCursor?.count!! >0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                myEstu= Usuario()
                myEstu.id=myCursor.getInt(myCursor.getColumnIndex(UsuarioContract.COLUMN_ID))
                myEstu.nombre=myCursor.getString(myCursor.getColumnIndex(UsuarioContract.COLUMN_NOMBRE))
                myEstu.apellido=myCursor.getString(myCursor.getColumnIndex(UsuarioContract.COLUMN_APELLIDO))
                myEstu.codGrado=myCursor.getString(myCursor.getColumnIndex(UsuarioContract.COLUMN_CODGRADO))
                myEstu.usuario=myCursor.getString(myCursor.getColumnIndex(UsuarioContract.COLUMN_USUARIO))

                myListEstu.add(myEstu)

                myCursor.moveToNext()
            }

            return myListEstu
        }
        return arrayListOf()
    }
}
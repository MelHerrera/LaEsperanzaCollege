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
        val result=mySqlDatabase?.delete(UsuarioContract.TABLE_NAME,"${UsuarioContract.COLUMN_ID}=?", arrayOf(usuarioId.toString()))

        if (result != null) {
            return result>0
        }
        return false
    }

    fun insertar(usuario:Usuario):Boolean
    {
        val datos= ContentValues()
        datos.put(UsuarioContract.COLUMN_NOMBRE,usuario.nombre)
        datos.put(UsuarioContract.COLUMN_APELLIDO,usuario.apellido)
        datos.put(UsuarioContract.COLUMN_USUARIO,usuario.usuario)
        datos.put(UsuarioContract.COLUMN_CONTRASE,usuario.contrase)
        datos.put(UsuarioContract.COLUMN_CODGRADO,usuario.codGrado)
        datos.put(UsuarioContract.COLUMN_TIPODEUSUARIOID,usuario.tipoDeUsuarioId)
        datos.put(UsuarioContract.COLUMN_SECCION,usuario.seccion)

        result=mySqlDatabase?.insert(UsuarioContract.TABLE_NAME,null,datos)

        return result!=(-1).toLong()
    }

    fun actualizar(usuario:Usuario):Boolean
    {
        val datos= ContentValues()
        datos.put(UsuarioContract.COLUMN_NOMBRE,usuario.nombre)
        datos.put(UsuarioContract.COLUMN_APELLIDO,usuario.apellido)
        datos.put(UsuarioContract.COLUMN_USUARIO,usuario.usuario)
        datos.put(UsuarioContract.COLUMN_CODGRADO,usuario.codGrado)
        datos.put(UsuarioContract.COLUMN_SECCION,usuario.seccion)

        if(usuario.contrase!=null && usuario.contrase!!.isNotEmpty())
            datos.put(UsuarioContract.COLUMN_CONTRASE,usuario.contrase)

        val query="${UsuarioContract.COLUMN_ID}=?"

        val res=mySqlDatabase?.update(
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

    fun actualizar(mUsuario:String,mPass:String?,usuarioId:Int):Boolean
    {
        val datos= ContentValues()
        datos.put(UsuarioContract.COLUMN_USUARIO,mUsuario)

        if(mPass!=null)
            datos.put(UsuarioContract.COLUMN_CONTRASE,mPass)

        val query="${UsuarioContract.COLUMN_ID}=?"

        val res=mySqlDatabase?.update(
            UsuarioContract.TABLE_NAME,
            datos,
            query,
            arrayOf(usuarioId.toString())
        )

        if (res != null) {
            return res>0
        }
        return false
    }

    fun actualizarImagen(mPhoto:ByteArray,usuarioId: Int?):Boolean
    {
        val datos= ContentValues()
        datos.put(UsuarioContract.COLUMN_IMAGEN,mPhoto)

        val selection="${UsuarioContract.COLUMN_ID}=?"

        val res=mySqlDatabase?.update(
            UsuarioContract.TABLE_NAME,
            datos,
            selection,
            arrayOf(usuarioId.toString())
        )

        if (res != null) {
            return res>0
        }
        return false
    }

    fun Buscar(usuario:String, contrase:String):Usuario?
    {
        val miUsuario= Usuario()
        val query="${UsuarioContract.COLUMN_USUARIO} =? AND ${UsuarioContract.COLUMN_CONTRASE} =?"
        val myCursor:Cursor?=mySqlDatabase?.query(UsuarioContract.TABLE_NAME,null,query, arrayOf(usuario,contrase),null,null,null)

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
                miUsuario.seccion=myCursor.getString(myCursor.getColumnIndex(UsuarioContract.COLUMN_SECCION))

                val mImagen=myCursor.getBlob(myCursor.getColumnIndex(UsuarioContract.COLUMN_IMAGEN))

                if(mImagen!=null)
                {
                    miUsuario.imagen=mImagen
                }

                myCursor.moveToNext()
            }

            return miUsuario
        }
        return null
    }

    fun listarEstudiantes():ArrayList<Usuario>
    {
        val myListEstu:ArrayList<Usuario> = ArrayList()
        var myEstu: Usuario
        val myQuery="${UsuarioContract.COLUMN_TIPODEUSUARIOID} =?"
        val myCursor=mySqlDatabase?.query(UsuarioContract.TABLE_NAME,null,myQuery, arrayOf("2"),null,null,null)

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
                myEstu.seccion=myCursor.getString(myCursor.getColumnIndex(UsuarioContract.COLUMN_SECCION))

                myListEstu.add(myEstu)

                myCursor.moveToNext()
            }

            return myListEstu
        }
        return arrayListOf()
    }
}
package com.app.laesperanzaedm.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.app.laesperanzaedm.R

class MyDbHelper(var context: Context, nombre:String, factory:SQLiteDatabase.CursorFactory?, version:Int):SQLiteOpenHelper(
    context,nombre,factory,version)
{
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(TipoDeUsuarioContract.SQL_CREATE_TABLE)
        db?.execSQL(GradoContract.SQL_CREATE_TABLE)
        db?.execSQL(UsuarioContract.SQL_CREATE_TABLE)
        db?.execSQL(UnidadContract.SQL_CREATE_TABLE)
        db?.execSQL(QuizContract.SQL_CREATE_TABLE)
        db?.execSQL(UsuarioQuizzContract.SQL_CREATE_TABLE)
        db?.execSQL(OpcionDeRespuestaContract.SQL_CREATE_TABLE)
        db?.execSQL(PreguntaContract.SQL_CREATE_TABLE)
        db?.execSQL(RespuestaContract.SQL_CREATE_TABLE)
        Inicializar(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        if(newVersion>oldVersion)
        {
            //borrar todas las tablas
            db?.execSQL(TipoDeUsuarioContract.SQL_CREATE_TABLE)
            db?.execSQL(GradoContract.SQL_CREATE_TABLE)
            db?.execSQL(UsuarioContract.SQL_CREATE_TABLE)
            db?.execSQL(UnidadContract.SQL_CREATE_TABLE)
            db?.execSQL(QuizContract.SQL_CREATE_TABLE)
            db?.execSQL(UsuarioQuizzContract.SQL_CREATE_TABLE)
            db?.execSQL(OpcionDeRespuestaContract.SQL_CREATE_TABLE)
            db?.execSQL(PreguntaContract.SQL_CREATE_TABLE)
            db?.execSQL(RespuestaContract.SQL_CREATE_TABLE)

            //volver a crearlas pero ya con una nueva version y esructura modificada
            onCreate(db)
        }

    }

    fun Inicializar(mySqlDatabase: SQLiteDatabase?)
    {

        //Add userType by default
        var datosadmin= ContentValues()
        datosadmin.put(TipoDeUsuarioContract.COLUMN_DESCRIPCION,"Admin")
        mySqlDatabase?.insert(TipoDeUsuarioContract.TABLE_NAME,null,datosadmin)

        var datosedtudi= ContentValues()
        datosedtudi.put(TipoDeUsuarioContract.COLUMN_DESCRIPCION,"Estudiante")
        mySqlDatabase?.insert(TipoDeUsuarioContract.TABLE_NAME,null,datosedtudi)

        //Add Grade by default

        var dat= ContentValues()
        dat.put(GradoContract.COLUMN_CODGRADO,"0grd")
        dat.put(GradoContract.COLUMN_DESCRIPCION,"Sin Grado")
        mySqlDatabase?.insert(GradoContract.TABLE_NAME,null,dat)

        var datos7= ContentValues()
        datos7.put(GradoContract.COLUMN_CODGRADO,"7mo")
        datos7.put(GradoContract.COLUMN_DESCRIPCION,"Séptimo Grado")
        mySqlDatabase?.insert(GradoContract.TABLE_NAME,null,datos7)

        var datos8= ContentValues()
        datos8.put(GradoContract.COLUMN_CODGRADO,"8vo")
        datos8.put(GradoContract.COLUMN_DESCRIPCION,"Octavo Grado")
        mySqlDatabase?.insert(GradoContract.TABLE_NAME,null,datos8)

        var datos9= ContentValues()
        datos9.put(GradoContract.COLUMN_CODGRADO,"9no")
        datos9.put(GradoContract.COLUMN_DESCRIPCION,"Noveno Grado")
        mySqlDatabase?.insert(GradoContract.TABLE_NAME,null,datos9)

        var datos10= ContentValues()
        datos10.put(GradoContract.COLUMN_CODGRADO,"10mo")
        datos10.put(GradoContract.COLUMN_DESCRIPCION,"Decimo Grado")
        mySqlDatabase?.insert(GradoContract.TABLE_NAME,null,datos10)

        var datos11= ContentValues()
        datos11.put(GradoContract.COLUMN_CODGRADO,"11mo")
        datos11.put(GradoContract.COLUMN_DESCRIPCION,"Undecimo Grado")
        mySqlDatabase?.insert(GradoContract.TABLE_NAME,null,datos11)

        //Add User Admin

        var rootUser= ContentValues()
        rootUser.put(UsuarioContract.COLUMN_NOMBRE,context.getString(R.string.adm_name))
        rootUser.put(UsuarioContract.COLUMN_APELLIDO,context.getString(R.string.adm_ape))
        rootUser.put(UsuarioContract.COLUMN_USUARIO,context.getString(R.string.adm_user))
        rootUser.put(UsuarioContract.COLUMN_CONTRASE,context.getString(R.string.adm_pass))
        rootUser.put(UsuarioContract.COLUMN_CODGRADO,1)
        rootUser.put(UsuarioContract.COLUMN_TIPODEUSUARIOID,1)

        mySqlDatabase?.insert(UsuarioContract.TABLE_NAME, null, rootUser)

        //estudiante de prueba 1
        var estudiante= ContentValues()
        estudiante.put(UsuarioContract.COLUMN_NOMBRE,"Dewin")
        estudiante.put(UsuarioContract.COLUMN_APELLIDO,"Haniel")
        estudiante.put(UsuarioContract.COLUMN_USUARIO,"dhan")
        estudiante.put(UsuarioContract.COLUMN_CONTRASE,"dhan2020#")
        estudiante.put(UsuarioContract.COLUMN_CODGRADO,"7mo")
        estudiante.put(UsuarioContract.COLUMN_TIPODEUSUARIOID,2)

        mySqlDatabase?.insert(UsuarioContract.TABLE_NAME, null, estudiante)

        //estudiante de prueba 1
        var estudiante1= ContentValues()
        estudiante1.put(UsuarioContract.COLUMN_NOMBRE,"Melkin")
        estudiante1.put(UsuarioContract.COLUMN_APELLIDO,"Herrera")
        estudiante1.put(UsuarioContract.COLUMN_USUARIO,"melh")
        estudiante1.put(UsuarioContract.COLUMN_CONTRASE,"dhan2020#")
        estudiante1.put(UsuarioContract.COLUMN_CODGRADO,"8vo")
        estudiante1.put(UsuarioContract.COLUMN_TIPODEUSUARIOID,2)

        mySqlDatabase?.insert(UsuarioContract.TABLE_NAME, null, estudiante1)

        //add opciones de respuesta de prueba
        var opciones=ContentValues()
        opciones.put(OpcionDeRespuestaContract.COLUMN_DESCRIPCION,"Opcion Multiple")
        mySqlDatabase?.insert(OpcionDeRespuestaContract.TABLE_NAME, null, opciones)

        //add opciones de respuesta de prueba
        var opciones1=ContentValues()
        opciones1.put(OpcionDeRespuestaContract.COLUMN_DESCRIPCION,"Falso y Verdadero")
        mySqlDatabase?.insert(OpcionDeRespuestaContract.TABLE_NAME, null, opciones1)

        //add opciones de respuesta de prueba
        var opciones2=ContentValues()
        opciones2.put(OpcionDeRespuestaContract.COLUMN_DESCRIPCION,"Arrastrar la Letra")
        mySqlDatabase?.insert(OpcionDeRespuestaContract.TABLE_NAME, null, opciones2)
    }
}
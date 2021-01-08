package com.app.laesperanzadao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.app.laesperanzaedm.database.*
import com.app.laesperanzaedm.model.Unidad
import com.app.laesperanzaedm.model.Usuario
import com.app.laesperanzaedm.model.UsuarioQuiz

class UsuarioQuizzDAO(context: Context) {
    private var mySqlDatabase: SQLiteDatabase?=null
    private var myDbAdapter: MyDbAdapter?=null

    init {
        myDbAdapter= MyDbAdapter(context)
        mySqlDatabase= myDbAdapter!!.openDatabase()
    }

    fun usuarioQuizzEstado(quizId: Int):Boolean
    {
        //-0.Pendient// el usuario aun no ha realizado el test
        // 1. Finalizado  // el usuario ya finalizo el test
        val query="${QuizContract.COLUMN_ID}=?"
        val result=mySqlDatabase?.query(UsuarioQuizzContract.TABLE_NAME,null,query, arrayOf(quizId.toString()),null,null,null,null)

        if(result?.count!! >0)
        {
            result.moveToFirst()
            return (result.getInt(result.getColumnIndex(QuizContract.COLUMN_ESTADO)))==1
        }
        return false
    }

    fun insertar(usuarioQuizz: UsuarioQuiz):Boolean
    {
        val datos= ContentValues()
        var result:Long?=null

        datos.put(UsuarioQuizzContract.COLUMN_QUIZID,usuarioQuizz.QuizId)
        datos.put(UsuarioQuizzContract.COLUMN_USUARIOID,usuarioQuizz.UsuarioId)
        datos.put(UsuarioQuizzContract.COLUMN_ESTADO,usuarioQuizz.Estado)

        result=mySqlDatabase?.insert(UsuarioQuizzContract.TABLE_NAME,null,datos)

        return result!=(-1).toLong()
    }


    fun actualizar(mUsuarioQuiz:UsuarioQuiz):Boolean
    {
        val datos= ContentValues()
        datos.put(UsuarioQuizzContract.COLUMN_QUIZID,mUsuarioQuiz.QuizId)
        datos.put(UsuarioQuizzContract.COLUMN_USUARIOID,mUsuarioQuiz.UsuarioId)
        datos.put(UsuarioQuizzContract.COLUMN_ESTADO,mUsuarioQuiz.Estado)

        val argumentos="${UsuarioQuizzContract.COLUMN_QUIZID}=?"

        val res=mySqlDatabase?.update(
            UsuarioQuizzContract.TABLE_NAME,
            datos,
            argumentos,
            arrayOf(mUsuarioQuiz.QuizId.toString())
        )

        if (res != null) {
            return res>0
        }
        return false
    }
    fun existe(usuarioId:Int, quizId: Int):UsuarioQuiz?
    {
        val mUsuarioQuizz=UsuarioQuiz()
        val argumentos="${UsuarioQuizzContract.COLUMN_QUIZID} =? AND ${UsuarioQuizzContract.COLUMN_USUARIOID} =?"
        val myCursor: Cursor?=mySqlDatabase?.query(UsuarioQuizzContract.TABLE_NAME,null,argumentos,
            arrayOf(quizId.toString(),usuarioId.toString()),
            null,
            null,
            null)

        if(myCursor?.count!! >0)
        {
                myCursor.moveToFirst()

                while (!myCursor.isAfterLast)
                {
                    mUsuarioQuizz.UsuarioQuizId =myCursor.getInt(myCursor.getColumnIndex(UsuarioQuizzContract.COLUMN_ID))
                    mUsuarioQuizz.UsuarioId=myCursor.getInt(myCursor.getColumnIndex(UsuarioQuizzContract.COLUMN_USUARIOID))
                    mUsuarioQuizz.QuizId=myCursor.getInt(myCursor.getColumnIndex(UsuarioQuizzContract.COLUMN_QUIZID))
                    mUsuarioQuizz.Estado=myCursor.getInt(myCursor.getColumnIndex(UsuarioQuizzContract.COLUMN_ESTADO))

                    myCursor.moveToNext()
                }

                return mUsuarioQuizz
            }
        return null
    }
}
package com.app.laesperanzadao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.app.laesperanzaedm.database.*
import com.app.laesperanzaedm.model.Quiz

class QuizDAO(context: Context) {
    var mySqlDatabase: SQLiteDatabase?=null
    var myDbAdapter: MyDbAdapter?=null
    var result:Long?=null

    init {
        myDbAdapter= MyDbAdapter(context)
        mySqlDatabase= myDbAdapter!!.openDatabase()
    }

    fun Insertar(quiz: Quiz):Boolean
    {
        var datos= ContentValues()
        datos.put(QuizContract.COLUMN_NOMBRE,quiz.nombre)
        datos.put(QuizContract.COLUMN_NUMUNIDAD,quiz.numUnidad)

        result=mySqlDatabase?.insert(QuizContract.TABLE_NAME,null,datos)

        return result!=(-1).toLong()
    }

    fun actualizar(quiz: Quiz):Boolean
    {
        var datos= ContentValues()
        datos.put(QuizContract.COLUMN_NOMBRE,quiz.nombre)

        var query="${QuizContract.COLUMN_ID}=?"

        var res=mySqlDatabase?.update(
            QuizContract.TABLE_NAME,
            datos,
            query,
            arrayOf(quiz.quizId.toString())
        )

        if (res != null) {
            return res>0
        }
        return false
    }

    fun ListarQuizzes():ArrayList<Quiz>
    {
        var myQuiz:Quiz?=null
        var myListQuizzes:ArrayList<Quiz> = arrayListOf()

        var res=mySqlDatabase?.query(QuizContract.TABLE_NAME,null,null,null,null,null,null,null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myQuiz= Quiz()
                myQuiz.quizId=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ID))
                myQuiz.nombre=res.getString(res.getColumnIndex(QuizContract.COLUMN_NOMBRE))

                myListQuizzes.add(myQuiz)

                res.moveToNext()
            }
        }
        return myListQuizzes
    }

    fun ListarQuizNuevos(Max:Int):ArrayList<Quiz>
    {
        var myQuiz:Quiz?=null
        var myListQuizzes:ArrayList<Quiz> = arrayListOf()
        var mySelection="${QuizContract.COLUMN_ID}>?"

        var res=mySqlDatabase?.query(QuizContract.TABLE_NAME,null,mySelection,
            arrayOf(Max.toString()),null,null,null,null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myQuiz= Quiz()
                myQuiz.quizId=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ID))
                myQuiz.nombre=res.getString(res.getColumnIndex(QuizContract.COLUMN_NOMBRE))

                myListQuizzes.add(myQuiz)

                res.moveToNext()
            }
        }
        return myListQuizzes
    }

    fun max():Int
    {
        var max:Int=-1
        var columns="Max(${QuizContract.COLUMN_ID})"

        var result=mySqlDatabase?.query(QuizContract.TABLE_NAME,
            arrayOf(columns),null,null,null,null,null,null)

        if(result?.count!!>0)
        {
            result.moveToFirst()

            while (!result.isAfterLast)
            {
                max=result.getInt(0)

                result.moveToNext()
            }

        }
        return max
    }
    fun CantPreguntas(quizId: Int):Int
    {

        var query:String="${PreguntaContract.COLUMN_QUIZZID}=?"

        var resul=mySqlDatabase?.query(PreguntaContract.TABLE_NAME, null,query, arrayOf(quizId.toString()),null,null,null,null)

        if(resul?.count!! >0)
        {
            return resul.count
        }
        return 0
    }

    fun CantQuizzes(numUnidad:Int):Int
    {

        var selection="${QuizContract.COLUMN_NUMUNIDAD}=?"

        var resul=mySqlDatabase?.query(QuizContract.TABLE_NAME, null,selection, arrayOf(numUnidad.toString()),null,null,null,null)

        if(resul?.count!! >0)
        {
            return resul.count
        }
        return 0
    }

    fun EstadoQuizz(quizzId:Int):Int
    {
        var pendiente:Int=0

        var query="${UsuarioQuizzContract.COLUMN_ID}=?"
        var result=mySqlDatabase?.query(UsuarioQuizzContract.TABLE_NAME,null,query, arrayOf(quizzId.toString()),null,null,null,null)

        if(result?.count!! >0)
        {
            pendiente= result.getInt(result.getColumnIndex(UsuarioQuizzContract.COLUMN_PENDIENTE))
        }
        return pendiente
    }

    fun BuscarQuizz(nombre:String): Quiz?
    {
        var miQuizz = Quiz()
        var query="${QuizContract.COLUMN_NOMBRE}=?"
        var myCursor: Cursor?=mySqlDatabase?.query(QuizContract.TABLE_NAME,null,query, arrayOf(nombre),null,null,null)

        if(myCursor?.count!! >0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                miQuizz.quizId =myCursor.getInt(myCursor.getColumnIndex(QuizContract.COLUMN_ID))
                miQuizz.nombre=myCursor.getString(myCursor.getColumnIndex(QuizContract.COLUMN_NOMBRE))

                myCursor.moveToNext()
            }

            return miQuizz
        }
        return null
    }
}
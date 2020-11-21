package com.app.laesperanzadao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
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
        val datos= ContentValues()
        datos.put(QuizContract.COLUMN_NOMBRE,quiz.nombre)
        datos.put(QuizContract.COLUMN_NUMUNIDAD,quiz.numUnidad)
        datos.put(QuizContract.COLUMN_ESTADO,quiz.estado)

        result=mySqlDatabase?.insert(QuizContract.TABLE_NAME,null,datos)

        return result!=(-1).toLong()
    }

    fun actualizar(quiz: Quiz):Boolean
    {
        val datos= ContentValues()
        datos.put(QuizContract.COLUMN_NOMBRE,quiz.nombre)

        val query="${QuizContract.COLUMN_ID}=?"

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
                myQuiz.numUnidad=res.getString(res.getColumnIndex(QuizContract.COLUMN_NUMUNIDAD))

                myListQuizzes.add(myQuiz)

                res.moveToNext()
            }
        }
        return myListQuizzes
    }

    fun listarQuizzesDePractica(myCodGrado:String?):ArrayList<Quiz>
    {
        //estados del quiz....   1.en progreso, 0. Finalizado, -1. No iniciado
        var myQuiz:Quiz?=null
        var myListQuizzes:ArrayList<Quiz> = arrayListOf()
        val mySelection= "SELECT q.NombreDeQuiz FROM ${QuizContract.TABLE_NAME} as q\n" +
                "INNER JOIN ${UnidadContract.TABLE_NAME} as u on q.NumUnidad==u.NumUnidad\n" +
                "INNER JOIN ${GradoContract.TABLE_NAME} as g on g.CodGrado==u.CodGrado\n" +
                "WHERE g.CodGrado==? AND q.estado=?" +
                "GROUP BY u.NumUnidad,g.CodGrado"

        var res=mySqlDatabase?.rawQuery(mySelection, arrayOf(myCodGrado,0.toString()),null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myQuiz= Quiz()
                myQuiz.quizId=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ID))
                myQuiz.nombre=res.getString(res.getColumnIndex(QuizContract.COLUMN_NOMBRE))
                myQuiz.numUnidad=res.getString(res.getColumnIndex(QuizContract.COLUMN_NUMUNIDAD))

                myListQuizzes.add(myQuiz)

                res.moveToNext()
            }
        }
        return myListQuizzes
    }

    fun listarQuizzesPrueba(myCodGrado:String?):ArrayList<Quiz>
    {
        //estados del quiz....   1.en progreso, 0. Finalizado, -1. No iniciado
        var myQuiz:Quiz?=null
        var myListQuizzes:ArrayList<Quiz> = arrayListOf()
        /*val mySelection= "SELECT ${QuizContract.COLUMN_ID},${QuizContract.COLUMN_NOMBRE},${QuizContract.COLUMN_ESTADO}  FROM ${QuizContract.TABLE_NAME} as q\n" +
                "INNER JOIN ${UnidadContract.TABLE_NAME} as u on q.NumUnidad==u.NumUnidad\n" +
                "INNER JOIN ${GradoContract.TABLE_NAME} as g on g.CodGrado==u.CodGrado\n" +
                "WHERE g.CodGrado==? AND q.estado=?" +
                " GROUP BY u.NumUnidad,g.CodGrado"*/

        val mySelection= "SELECT * FROM ${QuizContract.TABLE_NAME} as q\n" +
                "INNER JOIN ${UnidadContract.TABLE_NAME} as u on q.NumUnidad==u.NumUnidad\n" +
                "INNER JOIN ${GradoContract.TABLE_NAME} as g on g.CodGrado==u.CodGrado\n" +
                "WHERE g.CodGrado==? AND q.estado=?"

        var res=mySqlDatabase?.rawQuery(mySelection, arrayOf(myCodGrado,1.toString()),null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myQuiz= Quiz()
                myQuiz.quizId=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ID))
                myQuiz.nombre=res.getString(res.getColumnIndex(QuizContract.COLUMN_NOMBRE))
                myQuiz.estado=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ESTADO))
                myQuiz.numUnidad=res.getString(res.getColumnIndex(QuizContract.COLUMN_NUMUNIDAD))
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
        //-1.Finalizado  0. Sin iniciar  1.En progreso
        var estado:Int=0

        var query="${QuizContract.COLUMN_ID}=?"
        var result=mySqlDatabase?.query(QuizContract.TABLE_NAME,null,query, arrayOf(quizzId.toString()),null,null,null,null)

        if(result?.count!! >0)
        {
            result.moveToFirst()
            estado= result.getInt(result.getColumnIndex(QuizContract.COLUMN_ESTADO))
        }
        return estado
    }

    fun enProgreso(quizId: Int):Boolean
    {
        var valores= ContentValues()
        valores.put(QuizContract.COLUMN_ESTADO,1)//-1.finalizado, 0.Sin Iniciar, 1.En Progreso

        var where="${QuizContract.COLUMN_ID}=?"

        var result=mySqlDatabase?.update(QuizContract.TABLE_NAME,valores,where, arrayOf(quizId.toString()))

        if (result != null) {
            return result>0
        }
        return false
    }

    fun finalizar(quizId: Int):Boolean
    {
        var valores= ContentValues()
        valores.put(QuizContract.COLUMN_ESTADO,-1)//-1.finalizado, 0.Sin Iniciar, 1.En Progreso

        var where="${QuizContract.COLUMN_ID}=?"

        var result=mySqlDatabase?.update(QuizContract.TABLE_NAME,valores,where, arrayOf(quizId.toString()))

        if (result != null) {
            return result>0
        }
        return false
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
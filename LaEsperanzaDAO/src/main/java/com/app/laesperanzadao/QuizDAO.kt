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
    private var result:Long?=null

    init {
        myDbAdapter= MyDbAdapter(context)
        mySqlDatabase= myDbAdapter!!.openDatabase()
    }

    fun insertar(quiz: Quiz):Boolean
    {
        val datos= ContentValues()
        datos.put(QuizContract.COLUMN_NOMBRE,quiz.nombre)
        datos.put(QuizContract.COLUMN_NUMUNIDAD,quiz.numUnidad)
        datos.put(QuizContract.COLUMN_ESTADO,quiz.estado)
        datos.put(QuizContract.COLUMN_PUNTAJE,quiz.puntaje)

        result=mySqlDatabase?.insert(QuizContract.TABLE_NAME,null,datos)

        return result!=(-1).toLong()
    }

    fun actualizar(quiz: Quiz):Boolean
    {
        val datos= ContentValues()
        datos.put(QuizContract.COLUMN_NOMBRE,quiz.nombre)
        datos.put(QuizContract.COLUMN_ID,quiz.quizId)
        datos.put(QuizContract.COLUMN_NUMUNIDAD,quiz.numUnidad)
        datos.put(QuizContract.COLUMN_ESTADO,quiz.estado)
        datos.put(QuizContract.COLUMN_PUNTAJE,quiz.puntaje)

        val query="${QuizContract.COLUMN_ID}=?"

        val res=mySqlDatabase?.update(
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

    fun listarQuizzes():ArrayList<Quiz>
    {
        var myQuiz: Quiz
        val myListQuizzes:ArrayList<Quiz> = arrayListOf()

        val res=mySqlDatabase?.query(QuizContract.TABLE_NAME,null,null,null,null,null,null,null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myQuiz= Quiz()
                myQuiz.quizId=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ID))
                myQuiz.nombre=res.getString(res.getColumnIndex(QuizContract.COLUMN_NOMBRE))
                myQuiz.numUnidad=res.getInt(res.getColumnIndex(QuizContract.COLUMN_NUMUNIDAD))
                myQuiz.puntaje=res.getInt(res.getColumnIndex(QuizContract.COLUMN_PUNTAJE))
                myQuiz.estado=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ESTADO))

                myListQuizzes.add(myQuiz)

                res.moveToNext()
            }
        }
        return myListQuizzes
    }

    fun listarQuizzesDePractica(myCodGrado:String?,mContext:Context):ArrayList<Quiz>
    {
        //estados del qui de la tabla usuarioquizz...   0.en progreso, 1. Finalizado
        var myQuiz:Quiz
        val myListQuizzes:ArrayList<Quiz> = arrayListOf()

        val mySelection= "SELECT * FROM ${QuizContract.TABLE_NAME} as q INNER JOIN " +
                "${UnidadContract.TABLE_NAME} as u on q.NumUnidad==u.NumUnidad\n" +
                "                INNER JOIN ${GradoContract.TABLE_NAME} as g on g.CodGrado==u.CodGrado\n" +
                "                INNER JOIN ${UsuarioQuizzContract.TABLE_NAME} as uq ON uq.QuizId==q.QuizId\n" +
                "                WHERE g.CodGrado==? AND uq.estado==?"

        val res=mySqlDatabase?.rawQuery(mySelection, arrayOf(myCodGrado,1.toString()),null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myQuiz= Quiz()
                myQuiz.quizId=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ID))
                myQuiz.estado=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ESTADO))
                myQuiz.nombre=res.getString(res.getColumnIndex(QuizContract.COLUMN_NOMBRE))
                myQuiz.numUnidad=res.getInt(res.getColumnIndex(QuizContract.COLUMN_NUMUNIDAD))
                myQuiz.puntaje=res.getInt(res.getColumnIndex(QuizContract.COLUMN_PUNTAJE))

                if(cantPreguntas(myQuiz.quizId!!)>0 && UsuarioQuizzDAO(mContext).usuarioQuizzEstado(myQuiz.quizId!!))
                    myListQuizzes.add(myQuiz)

                res.moveToNext()
            }
        }
        return myListQuizzes
    }

    fun listarQuizzesPrueba(myCodGrado:String?,mContext:Context):ArrayList<Quiz>
    {
        //estados del quiz....   1.en progreso, 0. Finalizado, -1. No iniciado
        var myQuiz:Quiz
        val myListQuizzes:ArrayList<Quiz> = arrayListOf()

        val mySelection= "SELECT * FROM ${QuizContract.TABLE_NAME} as q\n" +
                "INNER JOIN ${UnidadContract.TABLE_NAME} as u on q.NumUnidad==u.NumUnidad\n" +
                "INNER JOIN ${GradoContract.TABLE_NAME} as g on g.CodGrado==u.CodGrado\n" +
                "WHERE g.CodGrado==? AND q.estado=?"

        val res=mySqlDatabase?.rawQuery(mySelection, arrayOf(myCodGrado,1.toString()),null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myQuiz= Quiz()
                myQuiz.quizId=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ID))
                myQuiz.nombre=res.getString(res.getColumnIndex(QuizContract.COLUMN_NOMBRE))
                myQuiz.estado=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ESTADO))
                myQuiz.numUnidad=res.getInt(res.getColumnIndex(QuizContract.COLUMN_NUMUNIDAD))
                myQuiz.puntaje=res.getInt(res.getColumnIndex(QuizContract.COLUMN_PUNTAJE))

                if(cantPreguntas(myQuiz.quizId!!)>0 && !UsuarioQuizzDAO(mContext).usuarioQuizzEstado(myQuiz.quizId!!))
                myListQuizzes.add(myQuiz)

                res.moveToNext()
            }
        }
        return myListQuizzes
    }

    fun listarQuizNuevos(Max:Int):ArrayList<Quiz>
    {
        var myQuiz:Quiz?
        val myListQuizzes:ArrayList<Quiz> = arrayListOf()
        val mySelection="${QuizContract.COLUMN_ID}>?"

        val res=mySqlDatabase?.query(QuizContract.TABLE_NAME,null,mySelection,
            arrayOf(Max.toString()),null,null,null,null)

        if(res?.count!! >0)
        {
            res.moveToFirst()

            while (!res.isAfterLast)
            {
                myQuiz= Quiz()
                myQuiz.quizId=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ID))
                myQuiz.nombre=res.getString(res.getColumnIndex(QuizContract.COLUMN_NOMBRE))
                myQuiz.estado=res.getInt(res.getColumnIndex(QuizContract.COLUMN_ESTADO))
                myQuiz.numUnidad=res.getInt(res.getColumnIndex(QuizContract.COLUMN_NUMUNIDAD))
                myQuiz.puntaje=res.getInt(res.getColumnIndex(QuizContract.COLUMN_PUNTAJE))

                myListQuizzes.add(myQuiz)

                res.moveToNext()
            }
        }
        return myListQuizzes
    }

    fun max():Int
    {
        var max:Int=-1
        val columns="Max(${QuizContract.COLUMN_ID})"

        val result=mySqlDatabase?.query(QuizContract.TABLE_NAME,
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
    fun cantPreguntas(quizId: Int):Int
    {

        val query:String="${PreguntaContract.COLUMN_QUIZZID}=?"

        val resul=mySqlDatabase?.query(PreguntaContract.TABLE_NAME, null,query, arrayOf(quizId.toString()),null,null,null,null)

        if(resul?.count!! >0)
        {
            return resul.count
        }
        return 0
    }

    fun cantQuizzes(numUnidad:Int):Int
    {

        val selection="${QuizContract.COLUMN_NUMUNIDAD}=?"

        val resul=mySqlDatabase?.query(QuizContract.TABLE_NAME, null,selection, arrayOf(numUnidad.toString()),null,null,null,null)

        if(resul?.count!! >0)
        {
            return resul.count
        }
        return 0
    }

    fun estadoQuizz(quizzId:Int):Int
    {
        //-1.Finalizado  0. Sin iniciar  1.En progreso
        var estado:Int=0

        val query="${QuizContract.COLUMN_ID}=?"
        val result=mySqlDatabase?.query(QuizContract.TABLE_NAME,null,query, arrayOf(quizzId.toString()),null,null,null,null)

        if(result?.count!! >0)
        {
            result.moveToFirst()
            estado= result.getInt(result.getColumnIndex(QuizContract.COLUMN_ESTADO))
        }
        return estado
    }

    fun enProgreso(quizId: Int):Boolean
    {
        val valores= ContentValues()
        valores.put(QuizContract.COLUMN_ESTADO,1)//-1.finalizado, 0.Sin Iniciar, 1.En Progreso

        val where="${QuizContract.COLUMN_ID}=?"

        val result=mySqlDatabase?.update(QuizContract.TABLE_NAME,valores,where, arrayOf(quizId.toString()))

        if (result != null) {
            return result>0
        }
        return false
    }

    fun finalizar(quizId: Int):Boolean
    {
        val valores= ContentValues()
        valores.put(QuizContract.COLUMN_ESTADO,-1)//-1.finalizado, 0.Sin Iniciar, 1.En Progreso

        val where="${QuizContract.COLUMN_ID}=?"

        val result=mySqlDatabase?.update(QuizContract.TABLE_NAME,valores,where, arrayOf(quizId.toString()))

        if (result != null) {
            return result>0
        }
        return false
    }

    fun buscarQuizz(nombre:String): Quiz?
    {
        val miQuizz = Quiz()
        val query="${QuizContract.COLUMN_NOMBRE}=?"
        val myCursor: Cursor?=mySqlDatabase?.query(QuizContract.TABLE_NAME,null,query, arrayOf(nombre),null,null,null)

        if(myCursor?.count!! >0)
        {
            myCursor.moveToFirst()

            while (!myCursor.isAfterLast)
            {
                miQuizz.quizId =myCursor.getInt(myCursor.getColumnIndex(QuizContract.COLUMN_ID))
                miQuizz.nombre=myCursor.getString(myCursor.getColumnIndex(QuizContract.COLUMN_NOMBRE))
                miQuizz.estado=myCursor.getInt(myCursor.getColumnIndex(QuizContract.COLUMN_ESTADO))
                miQuizz.numUnidad=myCursor.getInt(myCursor.getColumnIndex(QuizContract.COLUMN_NUMUNIDAD))
                miQuizz.puntaje=myCursor.getInt(myCursor.getColumnIndex(QuizContract.COLUMN_PUNTAJE))

                myCursor.moveToNext()
            }

            return miQuizz
        }
        return null
    }

    fun eliminarQuiz(quizId: Int?):Boolean
    {
        val result=mySqlDatabase?.delete(QuizContract.TABLE_NAME,"${QuizContract.COLUMN_ID}=?", arrayOf(quizId.toString()))

        if (result != null) {
            return result>0
        }
        return false
    }
}
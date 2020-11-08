package com.app.laesperanzaedm.database

class UsuarioQuizzContract {
    companion object
    {
        const val TABLE_NAME="UsuarioQuizz"
        const val COLUMN_ID="UsuarioQuizzId"
        const val COLUMN_QUIZID=QuizContract.COLUMN_ID
        const val COLUMN_USUARIOID=UsuarioContract.COLUMN_ID
        const val COLUMN_PENDIENTE="Pendiente"


        val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_PENDIENTE INT," +
                    "$COLUMN_USUARIOID INTEGER,"+
                    "$COLUMN_QUIZID INTEGER" +
                    ")"

        val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
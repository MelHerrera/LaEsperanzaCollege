package com.app.laesperanzaedm.database

class QuizContract {
    companion object
    {
        const val TABLE_NAME="Quiz"
        const val COLUMN_ID="QuizId"
        const val COLUMN_NOMBRE="NombreDeQuiz"
        const val COLUMN_NUMUNIDAD=UnidadContract.COLUMN_NUMUNIDAD
        const val COLUMN_ESTADO="Estado"
        const val COLUMN_PUNTAJE="Puntaje"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "$COLUMN_NOMBRE TEXT,"+
                    "$COLUMN_NUMUNIDAD TEXT,"+
                    "$COLUMN_ESTADO INTEGER," +
                    "$COLUMN_PUNTAJE INTEGER" +
                    ")"

        const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
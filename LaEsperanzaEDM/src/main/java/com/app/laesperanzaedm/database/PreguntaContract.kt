package com.app.laesperanzaedm.database

class PreguntaContract {
    companion object
    {
        const val TABLE_NAME="Pregunta"
        const val COLUMN_ID="PreguntaId"
        const val COLUMN_DESCRIPCION="Descripcion"
        const val COLUMN_QUIZZID=QuizContract.COLUMN_ID
        const val COLUMN_OPCIONDERESPUESTAID=OpcionDeRespuestaContract.COLUMN_ID

        val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "$COLUMN_DESCRIPCION TEXT,"+
                    "$COLUMN_QUIZZID INTEGER,"+
                    "$COLUMN_OPCIONDERESPUESTAID INTEGER"+
                    ")"

        val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
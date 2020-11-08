package com.app.laesperanzaedm.database

class RespuestaContract {
    companion object
    {
        const val TABLE_NAME="Respuesta"
        const val COLUMN_ID="RespuestaId"
        const val COLUMN_DESCRIPCION="Descripcion"
        const val COLUMN_CORRECTA="Correcta"
        const val COLUMN_PREGUNTAID=PreguntaContract.COLUMN_ID

        val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "$COLUMN_DESCRIPCION TEXT,"+
                    "$COLUMN_CORRECTA INTEGER,"+
                     "$COLUMN_PREGUNTAID INTEGER"+
                    ")"

        val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
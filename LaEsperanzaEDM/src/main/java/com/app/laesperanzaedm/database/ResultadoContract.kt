package com.app.laesperanzaedm.database

class ResultadoContract {
    companion object
    {
        const val TABLE_NAME="ResultadoQuiz"
        const val COLUMN_ID="ResultadoId"
        const val COLUMN_PREGUNTAID= PreguntaContract.COLUMN_ID
        const val COLUMN_RESPUESTASID="RespuestasId"
        const val COLUMN_USUARIOQUIZID=UsuarioQuizzContract.COLUMN_ID


        const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "$COLUMN_PREGUNTAID INTEGER,"+
                    "$COLUMN_RESPUESTASID TEXT,"+
                    "$COLUMN_USUARIOQUIZID INTEGER"+
                    ")"

        const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
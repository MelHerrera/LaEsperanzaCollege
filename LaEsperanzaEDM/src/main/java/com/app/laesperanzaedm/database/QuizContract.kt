package com.app.laesperanzaedm.database

class QuizContract {
    companion object
    {
        const val TABLE_NAME="Quiz"
        const val COLUMN_ID="QuizId"
        const val COLUMN_NOMBRE="NombreDeUnidad"
        const val COLUMN_NUMUNIDAD=UnidadContract.COLUMN_NUMUNIDAD

        val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "$COLUMN_NOMBRE TEXT,"+
                    "$COLUMN_NUMUNIDAD TEXT"+
                    ")"

        val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
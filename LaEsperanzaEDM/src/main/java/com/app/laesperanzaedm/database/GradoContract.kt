package com.app.laesperanzaedm.database

class GradoContract {
    companion object
    {
        const val TABLE_NAME="Grado"
        const val COLUMN_CODGRADO="CodGrado"
        const val COLUMN_DESCRIPCION="Descripcion"

        val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_CODGRADO TEXT,"+
                    "$COLUMN_DESCRIPCION TEXT"+
                    ")"

        val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
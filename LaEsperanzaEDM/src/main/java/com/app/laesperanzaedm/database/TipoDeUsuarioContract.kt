package com.app.laesperanzaedm.database

class TipoDeUsuarioContract {

    companion object
    {
        const val TABLE_NAME="TipoDeUsuario"
        const val COLUMN_ID="TipoDeUsuarioId"
        const val COLUMN_DESCRIPCION="Descripcion"

        val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_DESCRIPCION TEXT"+")"

        val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
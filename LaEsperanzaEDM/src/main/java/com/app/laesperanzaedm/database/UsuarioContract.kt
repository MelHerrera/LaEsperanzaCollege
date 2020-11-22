package com.app.laesperanzaedm.database

class UsuarioContract {
    companion object
    {
        const val TABLE_NAME="Usuario"
        const val COLUMN_ID="UsuarioId"
        const val COLUMN_NOMBRE="Nombre"
        const val COLUMN_APELLIDO="Apellido"
        const val COLUMN_USUARIO="Usuario"
        const val COLUMN_CONTRASE="Contraseña"
        const val COLUMN_SECCION="Seccion"
        const val COLUMN_IMAGEN="Foto"
        const val COLUMN_CODGRADO= GradoContract.COLUMN_CODGRADO
        const val COLUMN_TIPODEUSUARIOID= TipoDeUsuarioContract.COLUMN_ID


        val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NOMBRE TEXT," +
                    "$COLUMN_APELLIDO TEXT," +
                    "$COLUMN_USUARIO TEXT," +
                    "$COLUMN_CONTRASE TEXT," +
                    "$COLUMN_SECCION TEXT, "+
                    "$COLUMN_CODGRADO TEXT,"+
                    "$COLUMN_IMAGEN BLOB,"+
                    "$COLUMN_TIPODEUSUARIOID INTEGER" +
                    ")"

        val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
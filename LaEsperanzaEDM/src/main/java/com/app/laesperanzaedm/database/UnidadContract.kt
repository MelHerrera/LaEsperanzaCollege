package com.app.laesperanzaedm.database

import com.app.laesperanzaedm.database.GradoContract

class UnidadContract {
companion object
{
    const val TABLE_NAME="Unidad"
    const val COLUMN_NUMUNIDAD="NumUnidad"
    const val COLUMN_DESCRIPCION="Descripcion"
    const val COLUMN_CODGRADO= GradoContract.COLUMN_CODGRADO

    val SQL_CREATE_TABLE =
        "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_NUMUNIDAD INTEGER," +
                "$COLUMN_DESCRIPCION TEXT,"+
                "$COLUMN_CODGRADO TEXT" +
                ")"

    val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}
}
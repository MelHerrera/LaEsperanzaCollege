package com.app.laesperanzaedm.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase

class MyDbAdapter(context: Context) {
    private val DATABASE_NAME:String ="EsperanzaCollege.db"
    private val DATABASE_VERSION:Int =1
    private var myDbHelper: MyDbHelper?= null

    init {
        myDbHelper= MyDbHelper(context,DATABASE_NAME,null,DATABASE_VERSION)
    }

    fun openDatabase():SQLiteDatabase? {
        return myDbHelper?.writableDatabase }

    fun close() { myDbHelper?.close() }
}
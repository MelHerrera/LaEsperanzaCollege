package com.app.laesperanzacollege

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences

class Preferencias {

    companion object
    {
         val sharedPrefsFileUser: String ="userPrefernces"
         val sharedPrefsFileIntro: String ="introPrefernces"
    }

    private fun sharedPrefs(myContext: Context,sharedPrefsFile:String):EncryptedSharedPreferences
    {
        return EncryptedSharedPreferences.create(
            myContext,
            sharedPrefsFile,
            LLaveMaestra.build(myContext),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    fun guardarSharedPrefsUser(myContext: Context, myUser: Set<String?>, myPass: Set<String?>):Boolean
    {
        try {
            val mySharedPrefs=sharedPrefs(myContext,sharedPrefsFileUser).edit()

            mySharedPrefs.putString(myUser.elementAt(0),myUser.elementAt(1))
            mySharedPrefs.putString(myPass.elementAt(0),myPass.elementAt(1))

            mySharedPrefs.apply()
        }
        catch (e:Exception)
        {
            return false
        }
        return true
    }
    fun guardarSharedPrefsIntro(myContext: Context, myIntro: Set<String?>):Boolean
    {
        try {
            val mySharedPrefs=sharedPrefs(myContext,sharedPrefsFileIntro).edit()

            mySharedPrefs.putInt(myIntro.elementAt(0),myIntro.elementAt(1)!!.toInt())

            mySharedPrefs.apply()
        }
        catch (e:Exception)
        {
            return false
        }
        return true
    }

    fun obtenerSharedPrefsUser(myContext:Context,myUserKey:String,myPassKey:String):MutableList<String>
    {
        val mySharedPrefs=sharedPrefs(myContext,sharedPrefsFileUser)
        val myData = mutableListOf<String>()

        if(mySharedPrefs.all.isNotEmpty())
        {

            mySharedPrefs.getString(myUserKey,"")?.let { myData.add(0, it) }
            mySharedPrefs.getString(myPassKey,"")?.let { myData.add(1, it) }
        }
        return myData
    }

    fun obtenerSharedPrefsIntro(myContext:Context,myIntroKey:String):Int
    {
        val mySharedPrefs=sharedPrefs(myContext,sharedPrefsFileIntro)
        var myData = 0

        if(mySharedPrefs.all.isNotEmpty())
        {

            mySharedPrefs.getInt(myIntroKey,0).let { myData=it }
        }
        return myData
    }

    fun limpiarSharedPrefs(myContext: Context,sharedPrefsFile:String):Boolean
    {
        try {
            val mySharedPrefs=sharedPrefs(myContext,sharedPrefsFile).edit()

            mySharedPrefs.clear()
            mySharedPrefs.apply()
        }catch (e:Exception)
        {
            return false
        }
        return true
    }

}
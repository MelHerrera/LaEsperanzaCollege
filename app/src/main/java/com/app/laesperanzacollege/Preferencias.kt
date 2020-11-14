package com.app.laesperanzacollege

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences

class Preferencias {
    private val sharedPrefsFile: String ="userPrefernces"

    private fun sharedPrefs(myContext: Context):EncryptedSharedPreferences
    {
        return EncryptedSharedPreferences.create(
            myContext,
            sharedPrefsFile,
            LLaveMaestra.build(myContext),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    fun guardarSharedPrefs(myContext: Context, myUser: Set<String?>, myPass: Set<String?>):Boolean
    {
        try {
            val mySharedPrefs=sharedPrefs(myContext).edit()

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

    fun obtenerSharedPrefs(myContext:Context,myUserKey:String,myPassKey:String):MutableList<String>
    {
        val mySharedPrefs=sharedPrefs(myContext)
        val myData = mutableListOf<String>()

        if(mySharedPrefs.all.isNotEmpty())
        {

            mySharedPrefs.getString(myUserKey,"")?.let { myData.add(0, it) }
            mySharedPrefs.getString(myPassKey,"")?.let { myData.add(1, it) }
        }
        return myData
    }

    fun limpiarSharedPrefs(myContext: Context):Boolean
    {
        try {
            val mySharedPrefs=sharedPrefs(myContext).edit()

            mySharedPrefs.clear()
            mySharedPrefs.apply()
        }catch (e:Exception)
        {
            return false
        }
        return true
    }

}
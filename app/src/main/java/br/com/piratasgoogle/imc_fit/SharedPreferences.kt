package br.com.piratasgoogle.imc_fit

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {
    private val myPreferences = context.getString(R.string.namePreferences)
    private val sharedPreferences = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE)

    fun editString(KEY_NAME: String, STR_TEXT: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_NAME, STR_TEXT)
        editor.apply()
    }

    fun removeString(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }

    fun returnString(KEY_NAME: String) : String {
        return sharedPreferences.getString(KEY_NAME, null).toString()
    }

    fun editBoolean(KEY_NAME: String, value: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(KEY_NAME, value)
        editor.apply()
    }

    fun verificaExistencia(KEY_NAME: String) : Boolean {
        return sharedPreferences.contains(KEY_NAME)
    }


}
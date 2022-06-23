package com.example.hbms

import android.content.Context

class LocalDataProvider(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("pref_app_1", Context.MODE_PRIVATE)

    fun saveData(key:String, value:String){
        sharedPreferences.edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun getData(key:String):String{
        return sharedPreferences.getString(key,"")?:""
    }

    fun removeData(key:String){
        sharedPreferences.edit().remove(key).apply()
    }

    fun clearAllData(){
        sharedPreferences.edit().clear().apply()
    }
}
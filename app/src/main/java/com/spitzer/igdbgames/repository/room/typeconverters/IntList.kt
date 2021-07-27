package com.spitzer.igdbgames.repository.room.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IntListConverter {
    @TypeConverter
    fun restoreList(listOfString: String): ArrayList<Int> {
        val listOfString: List<String> =
            Gson().fromJson(listOfString, object : TypeToken<List<String>>() {}.type)
        var returnList: ArrayList<Int> = arrayListOf()
        listOfString.forEach {
            returnList.add(it.toInt())
        }
        return returnList
    }

    @TypeConverter
    fun saveList(list: ArrayList<Int>?): String {
        var listOfString: ArrayList<String> = arrayListOf()
        list?.forEach {
            listOfString.add(it.toString())
        }
        return Gson().toJson(listOfString)
    }
}

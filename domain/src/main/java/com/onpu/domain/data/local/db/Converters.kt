package com.onpu.domain.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromMapOfStringToJson(value: Map<String, String>): String = Gson().toJson(value)
    @TypeConverter
    fun fromStringToMapOfString(value: String): Map<String, String> = Gson().fromJson(value, object : TypeToken<Map<String,String>>(){}.type)
}
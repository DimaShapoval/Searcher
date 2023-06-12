package com.onpu.core.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.onpu.domain.util.CalendarTypeAdapter
import java.util.*

class Mapper {

    fun gson(): Gson = GsonBuilder()
        .registerTypeAdapter(Calendar::class.java, CalendarTypeAdapter())
        .registerTypeAdapter(GregorianCalendar::class.java, CalendarTypeAdapter())
        .create()

    private inline fun <reified T : Any> GsonBuilder.registerTypeAdapter(
        noinline getKey: (T) -> String,
        noinline getVal: (String) -> T?,
        defValue: T?
    ) = apply { registerTypeAdapter(T::class.java, StringTypeAdapter(getKey, getVal, defValue)) }

    inline fun <reified T : Any> GsonBuilder.registerTypeAdapter(
        noinline getKey: (T) -> Int,
        noinline getVal: (Int) -> T?) = apply {
        registerTypeAdapter(T::class.java, IntTypeAdapter(getKey, getVal))
    }

    class IntTypeAdapter<T : Any>(
        private val getKey: (T) -> Int,
        private val getVal: (Int) -> T?
    ) : TypeAdapter<T>() {
        override fun read(reader: JsonReader) = reader.readInt(getVal)
        override fun write(writer: JsonWriter, value: T?) = writer.writeInt(value, getKey)

        private fun <T : Any> JsonReader.readInt(getVal: (Int) -> T?): T? =
            if (peek() == JsonToken.NULL) run { nextNull(); null }
            else getVal(nextInt())

        private fun <T : Any> JsonWriter.writeInt(value: T?, getKey: (T) -> Int) {
            value?.let { value(getKey(it)) } ?: nullValue()
        }
    }

    class StringTypeAdapter<T : Any>(
        private val getKey: (T) -> String,
        private val getVal: (String) -> T?,
        private val defValue: T?
    ) : TypeAdapter<T>() {
        override fun read(reader: JsonReader) = reader.readString(getVal, defValue)
        override fun write(writer: JsonWriter, value: T?) = writer.writeString(value, getKey)

        private fun <T : Any> JsonReader.readString(getVal: (String) -> T?, defValue: T?): T? =
            when {
                peek() == JsonToken.STRING -> getVal(nextString())
                peek() == JsonToken.NUMBER -> getVal(nextInt().toString())
                else -> run { if (peek() == JsonToken.BOOLEAN) nextBoolean() else nextNull(); defValue }
            }

        private fun <T : Any> JsonWriter.writeString(value: T?, getKey: (T) -> String) {
            value?.let { value(getKey(it)) } ?: nullValue()
        }
    }
}
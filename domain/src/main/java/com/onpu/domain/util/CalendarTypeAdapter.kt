package com.onpu.domain.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken.NUMBER
import com.google.gson.stream.JsonToken.STRING
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.util.*

class CalendarTypeAdapter : TypeAdapter<Calendar>() {

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, value: Calendar?) {
        if (value == null) writer.nullValue()
        else writer.value(value.formatDate(serverFormat))
    }

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): Calendar? = when (jsonReader.peek()) {
        NUMBER -> jsonReader.nextLong().toCalendarUtc
        STRING -> jsonReader.nextString().toCalendarOrNull(timeZone = TimeZone.getTimeZone("UTC"))
        else -> jsonReader.nextNull().run { null }
    }
}
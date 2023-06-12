package com.onpu.domain.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

fun<A : Annotation> Any.annotation(annotation: Class<A>) = this::class.java.getAnnotation(annotation)
val Any.serializedName get() = annotation(SerializedName::class.java).value

fun<T> Any.sync(block: () -> T) = synchronized(this, block)

inline fun runIf(predicate: Boolean, run: () -> Unit) {
    if (predicate) run()
}

inline fun<T> tryOr(default: T, get: () -> T): T = try {
    get()
} catch (e: Exception) {
//    e.printStackTrace()
    default
}
inline fun<T> tryOr(default: (Exception) -> T, get: () -> T): T = try {
    get()
} catch (e: Exception) {
//    e.printStackTrace()
    default(e)
}

inline fun<T> tryOrNull(get: () -> T) = tryOr(null, get)

inline fun <T> T?.use(block: T.() -> Unit) = this?.run(block) ?: Unit

fun <T> textToJson(value: T): String = Gson().toJson(value)

fun <T> T.toJson(): String = buildGson().toJson(this)

inline fun <reified T> fromJson(s: String?): T = buildGson().fromJson(s, T::class.java)

fun buildGson(): Gson = GsonBuilder()
    .create()

inline fun <reified T> jsonToObj(value: String): T = Gson()
    .fromJson(value, T::class.java)

inline fun <reified T> String.toObject(): T = Gson()
    .fromJson(this, T::class.java)


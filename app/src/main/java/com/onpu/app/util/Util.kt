package com.onpu.app.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build.*
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.util.Patterns
import android.webkit.RenderProcessGoneDetail
import android.widget.ImageView
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.reflect.KClass


val pendingIntentFlagUpdate
    get() = if (VERSION.SDK_INT >= VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_UPDATE_CURRENT

fun<T : Any> T.simpleName() = this::class.simpleName.orEmpty()

@SuppressLint("UseCompatLoadingForDrawables")
fun ImageView.loadAppsIcon(packageName: String, appIconResId : Int) = load(
    try {
        context.packageManager.getApplicationIcon(packageName)
    } catch (e: Exception) {
        context.getDrawable(appIconResId)
    }
)

fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun ImageView.load(model: Any?, error: Int = 0) = Glide
    .with(this)
    .load(model)
    .error(error)
    .into(this)
    .view

val Context.userCountry: String?
    get() {
        try {
            val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simCountry = tm.simCountryIso
            if (simCountry != null && simCountry.length == 2) { // SIM country code is available
                return simCountry.lowercase(Locale.US)
            } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                val networkCountry = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                    return networkCountry.lowercase(Locale.US)
                }
            }
        } catch (e: Exception) {
        }
        return null
    }

val locale: String
    get() = if (VERSION.SDK_INT >= VERSION_CODES.N) Resources.getSystem().configuration.locales.get(0).language
    else Resources.getSystem().configuration.locale.language

val RenderProcessGoneDetail?.isDidCrash: Boolean get() {
    if (this == null) return false
    return if (VERSION.SDK_INT >= VERSION_CODES.O) didCrash() else false
}

fun Activity.rateApp() = try {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
} catch (e: ActivityNotFoundException) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        )
    )
}

val deviceManufacturerAndModel: String get() =  MODEL.addManufacturer()

private fun String.addManufacturer() = MANUFACTURER.let { if (startsWith(it)) this else "$this $it" }

@Suppress("DEPRECATION")
inline fun String?.asHtml(): Spanned = orEmpty().let {
    if (VERSION.SDK_INT < VERSION_CODES.N) Html.fromHtml(it)
    else Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
}

fun<A : Annotation> Any.annotation(annotation: KClass<A>) = this::class.java.getAnnotation(annotation.java)

val Any.serializedName get() = annotation(SerializedName::class)?.value.orEmpty()

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

inline fun tryOr(default: () -> Unit, get: () -> Unit): Unit = try {
    get()
} catch (e: Exception) {
    default()
}

inline fun tryTo(run: () -> Unit): Unit = tryOr(Unit, run)

inline fun<T> tryOrNull(get: () -> T) = tryOr(null, get)

inline fun <T> T?.make(block: T.() -> Unit) = this?.run(block) ?: Unit

fun<T> Intent?.serializableExtra(
    key: String, default: T
) = tryOr(default) { this?.getSerializableExtra(key) as T }

fun<T> Intent?.listExtra(key: String): List<T> = tryOr(listOf()) {
    (this?.getSerializableExtra(key) as Array<T>).toList()
}

fun<T> Intent?.mutableListExtra(key: String): MutableList<T> = listExtra<T>(key).toMutableList()

fun Editable.toIntOrZero(): Int {
    return try {
        if (isEmpty()) {
            0
        } else {
            toString().toInt()
        }
    } catch (e: Exception) {
        0
    }
}

fun Resources.getHtmlSpannedString(@StringRes id: Int): Spanned = getString(id).toHtmlSpan()

fun Resources.getHtmlSpannedString(@StringRes id: Int, vararg formatArgs: Any): Spanned = getString(id, *formatArgs).toHtmlSpan()

fun Resources.getQuantityHtmlSpannedString(@PluralsRes id: Int, quantity: Int): Spanned = getQuantityString(id, quantity).toHtmlSpan()

fun Resources.getQuantityHtmlSpannedString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): Spanned = getQuantityString(id, quantity, *formatArgs).toHtmlSpan()

fun String.toHtmlSpan(): Spanned = if (VERSION.SDK_INT >= VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
} else {
    Html.fromHtml(this)
}

fun <T> List<T>.trimStart(index: Int): List<T> {
    val end = if (size < index) size else index
    return slice(0 until end)
}
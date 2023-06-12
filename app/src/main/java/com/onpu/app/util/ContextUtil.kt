package com.onpu.app.util

//import okio.internal.commonAsUtf8ToByteArray
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.Uri
import android.net.VpnService
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.onpu.domain.util.isMinusOne
import okio.internal.commonAsUtf8ToByteArray
import java.io.File
import java.io.InputStream
import java.net.URLConnection
import java.util.*

private const val PHONE_URL = "tel:"

inline val Context.androidId
    get() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID).uppercase()

inline val Context.uuid
    get() = UUID.nameUUIDFromBytes(androidId.commonAsUtf8ToByteArray()).toString()
//inline val Context.uuid get () = "3289a808-3d06-39f8-abda-c4d808a4c0a7"

fun Fragment.openThisAppInPlayMarket() = requireContext().openThisAppInPlayMarket()
fun Context.openThisAppInPlayMarket(){
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
        )
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.shareLogFile(file: File): Unit {
    val uri = uriForFile(file)
    val intent = shareFileIntent(uri)
    queryIntentActivities(uri, intent)
    startActivity(intent)
}

fun Fragment.getDrawableFromAssetsByName(name: String) = requireContext().getDrawableFromAssetsByName(name)
fun Context.getDrawableFromAssetsByName(name: String): Drawable? {
    var inputStream: InputStream? = null;
    var res: Drawable? = null
    inputStream = assets.open("$name.png")
    res = Drawable.createFromStream(inputStream, null)
    inputStream.close()
    return res
}
fun Context.vpnProfileOfThisAppIsExist(): Boolean = VpnService.prepare(this) == null
fun Fragment.vpnProfileOfThisAppIsExist(): Boolean = requireContext().vpnProfileOfThisAppIsExist()


fun<I> Fragment.requestAddVpnProfile(getContent: ActivityResultLauncher<I>) = requireActivity().requestAddVpnProfile(getContent)
fun<I> Context.requestAddVpnProfile(getContent: ActivityResultLauncher<I>): Intent =
    VpnService.prepare(this).also { getContent.launch(null) }

fun Context.fromContentUriToFileUri(uri: Uri): Uri? {
    var myFileUri = Uri.EMPTY
    val cursor = contentResolver.query(
        uri,
        arrayOf(android.provider.MediaStore.Images.ImageColumns.DATA),
        null,
        null,
        null
    )
    if (cursor?.moveToFirst() == true) myFileUri = Uri.parse(cursor.getString(0))

    cursor?.close()
    return myFileUri
}

val Context.locationManager get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager
val Context.isEnabledNetworkProvider get() = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
val Context.isEnabledLocationProvider get() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
val Context.locationNotGranted
    get() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
val Context.locationHasBeenDenied
    get() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
val Context.locationGranted get() = !locationNotGranted

val Context.isLocationEnabled get() = isEnabledNetworkProvider && isEnabledLocationProvider

inline val Context.layoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

inline val Context.inputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Context.clipboardManager get() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

inline val Context.orientation get() = this.resources.configuration.orientation

inline fun Context.layoutInflater(@LayoutRes layoutRes: Int, root: ViewGroup? = null): View =
    LayoutInflater
        .from(this).inflate(layoutRes, root)

fun Context.string(@StringRes stringId: Int) = getString(stringId)
fun Context.string(@StringRes stringId: Int, vararg formatArgs: Any?) =
    getString(stringId, *formatArgs)

fun Context.spanned(@StringRes stringId: Int) = string(stringId).asHtml()
fun Context.spanned(@StringRes stringId: Int, vararg formatArgs: Any?) =
    string(stringId, *formatArgs).asHtml()

fun Context.quantityString(@PluralsRes id: Int, quantity: Int) = resources
    .getQuantityString(id, quantity)

fun Context.quantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?) = resources
    .getQuantityString(id, quantity, *formatArgs)

fun Context.quantitySpanned(@PluralsRes id: Int, quantity: Int) = resources
    .getQuantityString(id, quantity).asHtml()

fun Context.quantitySpanned(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?) = resources
    .getQuantityString(id, quantity, *formatArgs).asHtml()

fun Context.stringArray(@ArrayRes resId: Int): Array<String> = resources.getStringArray(resId)

fun Context.statusBarColor(@ColorRes colorId: Int) {
    if (this is Activity) statusBarColor(colorId)
}

fun Context.assetsReadText(fileName: String) = assets.open(fileName).bufferedReader()
    .use { it.readText().apply { it.close() } }

val Context.pasteFromClipboard
    get() = tryOrNull {
        clipboardManager.primaryClip?.getItemAt(0)?.text
    }
val Context.pasteStringFromClipboard get() = pasteFromClipboard.toString()

fun Context.pasteFromClipboard(paste: (CharSequence) -> Unit) = pasteFromClipboard
    ?.let(paste) ?: Unit

fun Context.pasteStringFromClipboard(paste: (String) -> Unit) = paste(pasteStringFromClipboard)

fun Context.copyToClipboard(text: String, message: String = "") = clipboardManager
    .setPrimaryClip(ClipData.newPlainText("", text))
    .run { if (message.isNotEmpty()) toast(message) }

fun Context.copyToClipboard(text: String, @StringRes message: Int) = clipboardManager
    .setPrimaryClip(ClipData.newPlainText("", text))
    .run { toast(message) }

fun Context.startPhoneIntent(
    number: String, @StringRes message: Int
) = startPhoneIntent(number, string(message))

fun Context.startPhoneIntent(number: String, message: String = "") {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("$PHONE_URL$number"))
    if (!isTelephonyEnabled()) return toast(message)
    tryOr({ copyToClipboard(number, message) }) { startActivity(intent) }
}

fun Context.isTelephonyEnabled(): Boolean {
    val tm = getSystemService(TELEPHONY_SERVICE) as? TelephonyManager
    return tm != null && tm.simState == TelephonyManager.SIM_STATE_READY
}

fun Context.navigationBarColor(@ColorRes colorId: Int) {
    if (this is Activity) navigationBarColor(colorId)
}

fun Context.toast(
    @StringRes resId: Int, vararg formatArgs: Any?, flag: Int = -1
) = toast(string(resId, formatArgs), flag)

fun Context.toast(@StringRes resId: Int, flag: Int = -1) = toast(string(resId), flag)

fun Context.toast(text: String, flag: Int = -1) = Toast
    .makeText(
        applicationContext,
        text,
        when {
            !flag.isMinusOne -> flag
            text.length > 50 -> Toast.LENGTH_LONG
            else -> Toast.LENGTH_SHORT
        }
    ).show()

@SuppressLint("UseCompatLoadingForDrawables")
fun Context.drawable(@DrawableRes resId: Int): Drawable =
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        resources.getDrawable(resId, theme)
    } else {
        resources.getDrawable(resId)
    }

fun Context.colorDrawable(@ColorRes resId: Int): Drawable = ColorDrawable(color(resId))

@SuppressLint("UseCompatLoadingForColorStateLists")
fun Context.colorStateList(@ColorRes resId: Int) = if (VERSION.SDK_INT >= VERSION_CODES.M) {
    getColorStateList(resId)
} else {
    resources.getColorStateList(resId)
}

fun Int.toHexStringOfColor(): String {
    return java.lang.String.format("#%06X", 0xFFFFFF and this)
}

@SuppressLint("UseCompatLoadingForColorStateLists")
fun Context.color(@ColorRes resId: Int) = if (VERSION.SDK_INT >= VERSION_CODES.M) {
    getColor(resId)
} else {
    resources.getColor(resId)
}

fun Context.font(@FontRes resId: Int) = if (VERSION.SDK_INT >= VERSION_CODES.O) {
    resources.getFont(resId)
} else {
    ResourcesCompat.getFont(this, resId)
}

inline val Context.authority get() = "$packageName.provider"

fun Context.uriForFile(file: File): Uri = FileProvider.getUriForFile(this, authority, file)

fun Context.uriForFile(file: File, function: (Uri) -> Unit) = function(uriForFile(file))

fun Context.openFile(file: File) = openFile(uriForFile(file))

fun Context.openFile(uri: Uri) = openFile(uri, contentResolver.getType(uri) ?: "*/*")


fun Context.openFile(uri: Uri, type: String) = Intent(Intent.ACTION_VIEW).run {
    setDataAndType(uri, type)
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        startActivity(this)
    } catch (e: Exception) {
    }
}

fun Context.openUrl(url: String?, message: String = "") {
    if (url.isNullOrEmpty()) return
    tryOr({ if (message.isNotEmpty()) toast(message) }) { startActivity(urlIntent(url)) }
}

fun Context.openUrl(url: String?, @StringRes resId: Int) {
    if (url.isNullOrEmpty()) return
    tryOr({ toast(resId) }) { startActivity(urlIntent(url)) }
}

private fun urlIntent(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

fun Context.writeFileToCache(uri: Uri?, name: String, onSuccess: (String) -> Unit) {
    if (uri == null) return
    contentResolver.openInputStream(uri)?.use { inp ->
        val buffer = ByteArray(8 * 1024)
        var length: Int

        File(cacheDir, "$name${uriFormat(uri)}").outputStream { out ->
            while (!inp.read(buffer).also { length = it }.isMinusOne) out.write(buffer, length)
            out.flush()
            out.close()
            inp.close()

            onSuccess(absolutePath)
        }
    }
}

fun Context.uriFormat(uri: Uri) = contentResolver
    .getType(uri)
    ?.run { ".${replaceBeforeLast(this[lastIndexOf("/") + 1], "")}" } ?: ".txt"

fun Context.shareFile(path: String) = shareFile(File(path))

fun Context.shareFile(file: File) = shareData(file) { uri, intent ->
    queryIntentActivities(uri, intent)
    startActivity(intent)
}

fun Context.shareData(
    file: File, function: (uri: Uri, intent: Intent) -> Unit
) = uriForFile(file) { function(it, shareIntent(it)) }

fun Context.queryIntentActivities(uri: Uri, intent: Intent) = packageManager
    .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    .forEach { grantUriPermission(it.activityInfo.packageName, uri, uriPermission) }

private val uriPermission
    get() = Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION

fun Context.shareIntent(file: File) = Intent.createChooser(intentActionSend(file), "Share File")
fun Context.shareIntent(uri: Uri) = Intent.createChooser(intentActionSend(uri), "Share File")

fun Context.intentActionSend(file: File) = Intent(Intent.ACTION_SEND).apply {
    type = URLConnection.guessContentTypeFromName(file.name)
    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    putExtra(Intent.EXTRA_STREAM, uriForFile(file))
}

fun Context.intentActionSend(uri: Uri) = Intent(Intent.ACTION_SEND).apply {
    type = contentResolver.getType(uri)
    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    putExtra(Intent.EXTRA_STREAM, uri)
}

fun Context.sendMail(
    @StringRes mail: Int,
    @StringRes subject: Int,
    @StringRes body: Int,
    @StringRes errorMessage: Int = -1,
    @StringRes intentTitle: Int = -1,
) = sendMail(
    mail,
    subject,
    string(body),
    errorMessage,
    intentTitle,
)

fun Context.sendMail(
    @StringRes mail: Int,
    @StringRes subject: Int,
    body: String,
    @StringRes errorMessage: Int = -1,
    @StringRes intentTitle: Int = -1,
) = sendMail(
    string(mail),
    string(subject),
    body,
    errorMessage,
    intentTitle,
)

fun Context.sendMail(
    mail: String,
    subject: String,
    body: String,
    @StringRes errorMessage: Int = -1,
    @StringRes intentTitle: Int = -1,
) = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
    .putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
    .putExtra(Intent.EXTRA_SUBJECT, subject)
    .putExtra(Intent.EXTRA_TEXT, body)
    .apply {
        tryOr({ if (!errorMessage.isMinusOne) toast(errorMessage) }) {
            startActivity(Intent.createChooser(this, string(intentTitle)))
        }
    }

fun Context.loadBitmap(resId: Int): Bitmap = BitmapFactory.decodeResource(resources, resId)

fun Context.statusBarHeight(): Int {
    return try {
        val resId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        resources.getDimensionPixelSize(resId)
    } catch (e: Exception) {
        0
    }
}

///////////////////////////////////////////---------------------------
@SuppressLint("QueryPermissionsNeeded")
fun Context.share(file: File): Unit {
    val uri = uriForFile(file)
    val intent = shareFileIntent(uri)
    queryIntentActivities(uri, intent)
    startActivity(intent)
}

//fun Context.uriForShareFile(file: File): Uri = FileProvider.getUriForFile(this, "$packageName.provider", file)

fun Context.shareFileIntent(uri: Uri): Intent = Intent
    .createChooser(intentActionSendFile(uri), "Share File")

fun Context.intentActionSendFile(uri: Uri): Intent = Intent(Intent.ACTION_SEND)
    .setType(contentResolver.getType(uri))
    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    .putExtra(Intent.EXTRA_STREAM, uri)

//@SuppressLint("QueryPermissionsNeeded")
//fun Context.queryIntentActivities(uri: Uri, intent: Intent) {
//    queryIntentActivities(intent).forEach {
//        grantUriPermission(
//            it.activityInfo.packageName,
//            uri,
//            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
//        )
//    }
//}

@SuppressLint("QueryPermissionsNeeded")
fun Context.queryIntentActivities(
    intent: Intent, flags: Int = PackageManager.MATCH_DEFAULT_ONLY,
): List<ResolveInfo> {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) packageManager.queryIntentActivities(
        intent,
        flags
    )
    else packageManager.queryIntentActivities(
        intent,
        PackageManager.ResolveInfoFlags.of(flags.toLong())
    )
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.shareText(text: String): Unit {
    val shareIntent = Intent.createChooser(
        Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            },
        null
    )
    startActivity(shareIntent)
}


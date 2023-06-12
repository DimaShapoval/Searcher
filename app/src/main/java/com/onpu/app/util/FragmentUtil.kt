package com.onpu.app.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.*
import androidx.fragment.app.Fragment
import java.io.File
import kotlin.reflect.KClass

fun Fragment.requestExternalStoragePermission() {
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}.launch(
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )
}

inline fun Fragment.layoutInflater(@LayoutRes layoutRes: Int, root: ViewGroup? = null): View = LayoutInflater
    .from(requireContext()).inflate(layoutRes, root)

fun Fragment.string(@StringRes stringId: Int) = requireContext().string(stringId)
fun Fragment.string(@StringRes stringId: Int, vararg formatArgs: Any?) = requireContext()
    .string(stringId, *formatArgs)

//fun Fragment.spanned(@StringRes stringId: Int) = string(stringId).asHtml()
//fun Fragment.spanned(@StringRes stringId: Int, vararg formatArgs: Any?) = string(stringId, *formatArgs).asHtml()

fun Fragment.quantityString(@PluralsRes id: Int, quantity: Int) = requireContext()
    .quantityString(id, quantity)
fun Fragment.quantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?) = requireContext()
    .quantityString(id, quantity, *formatArgs)

fun Fragment.quantitySpanned(@PluralsRes id: Int, quantity: Int) = requireContext()
    .quantitySpanned(id, quantity)
fun Fragment.quantitySpanned(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?) = requireContext()
    .quantitySpanned(id, quantity, *formatArgs)

fun Fragment.stringArray(@ArrayRes arrayId: Int) = requireContext().stringArray(arrayId)

fun Fragment.clearAllFocus() = requireActivity().clearAllFocus()
fun Fragment.hideKeyboard() = requireActivity().hideKeyboard()
fun Fragment.hideKeyboardAndClearFocus() = requireActivity().hideKeyboardAndClearFocus()

@SuppressLint("UseCompatLoadingForColorStateLists")
fun Fragment.colorStateList(@ColorRes resId: Int) = requireContext().colorStateList(resId)

fun Fragment.color(@ColorRes resId: Int) = requireContext().color(resId)

fun Fragment.setupUI(v: View?) = requireActivity().setupUI(v)

fun Fragment.openUrl(url: String?, message: String = "") = requireContext().openUrl(url, message)
fun Fragment.openUrl(url: String?, @StringRes resId: Int) = requireContext().openUrl(url, resId)

fun<T: Activity> Fragment.startActivity(activity: KClass<T>, flags: Int = -1) = requireActivity()
    .startActivity(activity, flags)

fun<T: Activity> Fragment.startActivity(
    activity: KClass<T>, flags: Int = -1, function: Intent.() -> Unit
) = requireActivity().startActivity(activity, flags, extras = null, function)

fun<T: Activity> Fragment.startActivityClearTask(activity: KClass<T>) = requireActivity()
    .startActivityClearTask(activity)

fun Fragment.toast(text: String, flag: Int = -1) = requireContext().toast(text, flag)
fun Fragment.toast(@StringRes resId: Int, flag: Int = -1) = requireContext().toast(resId, flag)
fun Fragment.toast(@StringRes resId: Int, vararg formatArgs: Any?, flag: Int = -1) = requireContext()
    .toast(resId, formatArgs, flag)

fun Fragment.writeFileToCache(uri: Uri?, name: String, onSuccess: (String) -> Unit) = requireContext()
    .writeFileToCache(uri, name, onSuccess)

fun Fragment.shareFile(path: String) = requireContext().shareFile(File(path))
fun Fragment.shareFile(file: File) = requireContext().shareFile(file)

fun Fragment.shareData(
    file: File, function: (uri: Uri, intent: Intent) -> Unit
) = requireContext().shareData(file, function)

fun Fragment.windowsBackground(@DrawableRes resId: Int) = requireActivity().windowsBackground(resId)

val Fragment.pasteFromClipboard get() = requireContext().pasteFromClipboard
val Fragment.pasteStringFromClipboard get() = requireContext().pasteStringFromClipboard

fun Fragment.pasteFromClipboard (paste: (CharSequence) -> Unit) = requireContext()
    .pasteFromClipboard(paste)
fun Fragment.pasteStringFromClipboard (paste: (String) -> Unit) = requireContext()
    .pasteStringFromClipboard(paste)

fun Fragment.copyToClipboard(text: String, message: String = "") = requireContext()
    .copyToClipboard(text, message)

fun Fragment.copyToClipboard(text: String, @StringRes message: Int) = requireContext()
    .copyToClipboard(text, message)

fun Fragment.startPhoneIntent(number: String, message: String = "") = requireContext()
    .startPhoneIntent(number, message)

fun Fragment.startPhoneIntent(number: String, @StringRes message: Int) = requireContext()
    .startPhoneIntent(number, message)

fun Fragment.sendMail(
    @StringRes mail: Int,
    @StringRes subject: Int,
    @StringRes body: Int,
    @StringRes errorMessage: Int = -1,
    @StringRes intentTitle: Int = -1,
) = requireContext().sendMail(mail, subject, body, errorMessage, intentTitle)

fun Fragment.sendMail(
    @StringRes mail: Int,
    @StringRes subject: Int,
    body: String,
    @StringRes errorMessage: Int = -1,
    @StringRes intentTitle: Int = -1,
) = requireContext().sendMail(mail, subject, body, errorMessage, intentTitle)

fun Fragment.sendMail(
    mail: String,
    subject: String,
    body: String,
    @StringRes errorMessage: Int = -1,
    @StringRes intentTitle: Int = -1,
) = requireContext().sendMail(mail, subject, body, errorMessage, intentTitle)

package com.onpu.app.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.io.File
import kotlin.reflect.KClass

inline fun Activity.onKeyboardVisibleListener(
    crossinline execute: (Boolean) -> Unit
) = findViewById<View>(android.R.id.content).let {
    it.viewTreeObserver.addOnGlobalLayoutListener {
        val r = Rect()
        it.getWindowVisibleDisplayFrame(r)
        val screenHeight = it.rootView.height
        val keypadHeight = screenHeight - r.bottom
        execute.invoke(keypadHeight > screenHeight * 0.15)
    }
}

fun ComponentActivity.requestExternalStoragePermission() {
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}.launch(
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        )
    )
}

fun Activity.scanPicturesForCursor(currentPhotoPath: String) {
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    val f = File(currentPhotoPath)
    val contentUri = Uri.fromFile(f)
    mediaScanIntent.data = contentUri
    sendBroadcast(mediaScanIntent)
}

@SuppressLint("ClickableViewAccessibility")
fun Activity.setupUI(v: View?) {
    if (v != null && v !is EditText) {
        v.setOnTouchListener { view, _ ->
            view.hideKeyboard()
            false
        }
    }
    if (v is ViewGroup) for (i in 0 until v.childCount) setupUI(v.getChildAt(i))
}

fun Activity.clearAllFocus() {
    window.decorView.clearFocus()
    if (this is FragmentActivity) {
        supportFragmentManager.fragments.forEach { if (it.isVisible) it.view?.clearFocus() }
    }
}

fun Activity.hideKeyboard() {
    window.decorView.hideKeyboard()
}

fun Activity.hideKeyboardAndClearFocus() {
    hideKeyboard()
    clearAllFocus()
}

fun FragmentActivity.fragmentByTag(tag: String) = supportFragmentManager.findFragmentByTag(tag)

@SuppressLint("RestrictedApi")
inline fun FragmentActivity.popBackStackOrOnBackPressed(
    crossinline onBackPressed: () -> Unit
) = supportFragmentManager.menuVisible()?.onBackPressed(onBackPressed) ?: onBackPressed()

fun Fragment.searchMenuVisible(): Fragment {
    var fragment: Fragment? = null
    childFragmentManager.menuVisible()?.let { fragment = it }
    return fragment ?: this
}


@SuppressLint("RestrictedApi")
fun FragmentManager.menuVisible(): Fragment? {
    for (it in fragments) if (it.isMenuVisible) return it.searchMenuVisible()
    return null
}

inline fun Fragment.onBackPressed(crossinline onBackPressed: () -> Unit) = when {
    childFragmentManager.backStackEntryCount > 0 -> childFragmentManager.popBackStack()
    parentFragmentManager.backStackEntryCount > 0 -> parentFragmentManager.popBackStack()
    else -> onBackPressed.invoke()
}

fun Activity.statusBarColor(@ColorRes colorId: Int) {
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) window.statusBarColor = color(colorId)
}

fun Activity.windowsBackground(@DrawableRes resId: Int) {
    window.setBackgroundDrawableResource(resId)
}

fun Activity.windowsBackgroundColor(@ColorRes resId: Int) =  window
    .setBackgroundDrawable(colorDrawable(resId))

fun Activity.navigationBarColor(@ColorRes colorId: Int) {
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) window.navigationBarColor = color(colorId)
}

fun<T: Activity> Activity.startActivity(
    activity: KClass<T>, flags: Int = -1, extras: Bundle? = null
) = startActivity(activity, flags, extras) { }

fun<T: Activity> Activity.startActivity(
    activity: KClass<T>, flags: Int = -1, extras: Bundle?, function: Intent.() -> Unit
) = startActivity(
    Intent(this, activity.java).apply {
        if (flags != -1) this.flags = flags
        if (extras != null) {
            putExtras(extras)
        }
        function(this)
    }
)

fun<T: Activity> Activity.startActivityClearTask(activity: KClass<T>) = startActivity(
    activity, Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
).run { finish() }

fun<T: Activity> Activity.startActivityClearTask(activity: KClass<T>, extras: Bundle?) = startActivity(
    activity,
    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK,
    extras
).run { finish() }

fun<T> Activity.serializableExtra(key: String, default: T) = intent.serializableExtra(key, default)
fun<T> Activity.listExtra(key: String): List<T> = intent.listExtra(key)
fun<T> Activity.mutableListExtra(key: String): MutableList<T> = intent.mutableListExtra(key)

inline fun ActivityResult.result(
    crossinline onCanceled: (Intent?) -> Unit = {  }, crossinline onOk: (Intent?) -> Unit
) {
    isCancel(onCanceled)
    isOk(onOk)
}

inline fun ActivityResult.isOk(crossinline onOk: (Intent?) -> Unit){
    if (resultCode == Activity.RESULT_OK) onOk(data)
}

inline fun ActivityResult.isCancel(crossinline onCanceled: (Intent?) -> Unit){
    if (resultCode == Activity.RESULT_CANCELED) onCanceled(data)
}
//fun Fragment.showBottomSheetDialog(
//    title: String,
//    message: String,
//    positiveButtonText: String,
//    positiveButtonOnClick: () -> Unit
//) = requireActivity().showBottomSheetDialog(title, message, positiveButtonText, positiveButtonOnClick)
//fun Activity.showBottomSheetDialog(
//    title: String,
//    message: String,
//    positiveButtonText: String,
//    positiveButtonOnClick: () -> Unit
//) {
//    val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
//    val bottomSheetDialogView = LayoutInflater.from(
//        applicationContext).inflate(
//        R.layout.layout_base_bottom_sheet_dialog,
//        this.findViewById(R.id.bottom_sheet_dialog_container)
//    )
//    bottomSheetDialogView.findViewById<TextView>(R.id.bottom_dialog_title).text = title
//    bottomSheetDialogView.findViewById<TextView>(R.id.bottom_dialog_message).text = message
//    bottomSheetDialogView.findViewById<Button>(R.id.first_action_button).apply {
//        text = positiveButtonText
//        onClickListener {
//            bottomSheetDialog.dismiss()
//            positiveButtonOnClick.invoke()
//        }
//    }
//    bottomSheetDialog.setContentView(bottomSheetDialogView)
//    bottomSheetDialog.show()
//}

//fun Fragment.showBottomSheetDialog(
//    title: String,
//    message: String,
//    positiveButtonText: String,
//    positiveButtonOnClick: () -> Unit,
//    negativeButtonText: String,
//    negativeButtonOnClick: () -> Unit
//) = requireActivity().showBottomSheetDialog(title, message, positiveButtonText, positiveButtonOnClick,negativeButtonText, negativeButtonOnClick)
//fun Activity.showBottomSheetDialog(
//    title: String,
//    message: String,
//    positiveButtonText: String,
//    positiveButtonOnClick: () -> Unit,
//    negativeButtonText: String,
//    negativeButtonOnClick: () -> Unit
//) {
//    val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
//    val bottomSheetDialogView =
//        LayoutInflater.from(this.applicationContext).inflate(
//            R.layout.layout_base_bottom_sheet_dialog,
//            this.findViewById(R.id.bottom_sheet_dialog_container)
//        )
//    bottomSheetDialogView.findViewById<TextView>(R.id.bottom_dialog_title).text = title
//    bottomSheetDialogView.findViewById<TextView>(R.id.bottom_dialog_message).text = message
//    bottomSheetDialogView.findViewById<Button>(R.id.first_action_button).text = positiveButtonText
//
//    bottomSheetDialogView.findViewById<Button>(R.id.first_action_button).apply {
//        text = positiveButtonText
//        onClickListener {
//            bottomSheetDialog.dismiss()
//            positiveButtonOnClick.invoke()
//        }
//    }
//    bottomSheetDialogView.findViewById<Button>(R.id.second_action_button).apply {
//        isVisible = true
//        text = negativeButtonText
//        onClickListener {
//            bottomSheetDialog.dismiss()
//            negativeButtonOnClick.invoke()
//        }
//    }
//    bottomSheetDialog.setContentView(bottomSheetDialogView)
//    bottomSheetDialog.show()
//}

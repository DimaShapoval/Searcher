package com.onpu.app.base.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

//
//fun Context.showAlertDialog(
//    title: String,
//    messageText:String,
//    positiveText : String = this.getString(R.string.ok),
//    positiveListener : () -> Unit? = {},
//    negativeText:String = "",
//    negativeListener: () -> Unit? = {}
//){
//    val builder = android.app.AlertDialog.Builder(this)
//        .setTitle(title)
//        .setMessage(messageText)
//        .setPositiveButton(positiveText, { dialog, which -> positiveListener.invoke() })
//    if(negativeText.isNotEmpty()) builder.setNegativeButton(negativeText, { dialog, which -> negativeListener.invoke() })
//    builder.show()
//}

    fun Context.showAlertDialog(
        titleId: Int,
        messageTextId:Int,
        positiveTextId : Int,
        positiveListener : DialogInterface.OnClickListener,
        negativeTextId:Int,
        negativeListener: DialogInterface.OnClickListener
    ){
        android.app.AlertDialog.Builder(this)
            .setTitle(titleId)
            .setMessage(messageTextId)
            .setPositiveButton(positiveTextId, positiveListener)
            .setNegativeButton(negativeTextId, negativeListener)
            .show()
    }

    fun Context.showAlertDialog(
        titleId: Int,
        messageTextId:Int,
        positiveTextId : Int,
        positiveListener : DialogInterface.OnClickListener,
        negativeTextId:Int,
        negativeListener: DialogInterface.OnClickListener,
        themeResId : Int
    ){
        android.app.AlertDialog.Builder(this, themeResId)
            .setTitle(titleId)
            .setMessage(messageTextId)
            .setPositiveButton(positiveTextId, positiveListener)
            .setNegativeButton(negativeTextId, negativeListener)
    }


private fun Context.customDialog(
    @LayoutRes layoutRes: Int, @StyleRes theme: Int = 0, dialog: View.(AlertDialog) -> Unit
) {
    val view = LayoutInflater
        .from(this)
        .inflate(layoutRes, LinearLayout(this))
    dialog.invoke(
        view,
        MaterialAlertDialogBuilder(this, theme)
            .setView(view)
            .create()
    )
}

private fun Context.customDialogPadding(layoutRes: Int, dialog: View.(AlertDialog) -> Unit) {
    val view = LayoutInflater
        .from(this)
        .inflate(layoutRes, LinearLayout(this))
    dialog.invoke(
        view,
        MaterialAlertDialogBuilder(this)
            .setView(view)
            .create()
            .apply { window?.setBackgroundDrawable(InsetDrawable(
                ColorDrawable(Color.TRANSPARENT),
                20,
                20,
                20,
                20)
            ) }
            .apply {
                val window = window
                val params = window?.attributes
                params?.gravity = Gravity.BOTTOM
                window?.attributes = params
            }
    )
}

private fun<T : ViewBinding> Context.returnCustomDialog(
    binding: T, dialog: View.(AlertDialog) -> AlertDialog
): AlertDialog {
    return dialog.invoke(
        binding.root,
        MaterialAlertDialogBuilder(this).setView(binding.root).create()
    )
}
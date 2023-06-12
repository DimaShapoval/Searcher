package com.onpu.app.base.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewbinding.ViewBinding
import com.onpu.app.util.setupUI

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected var binding: VB? = null

    protected abstract fun inflate(inflater: LayoutInflater): VB

    val isNightModeEnable
        get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

//    private val isNightMode get() = ComponentProvider.instance.preferences.isNightMode

    override fun onCreate(savedInstanceState: Bundle?) {
//        when (ComponentProvider.instance.preferences.theme) {
//            ThemeEnums.DARK.toString() -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            ThemeEnums.LIGHT.toString() -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            else -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//            }
//        }
        super.onCreate(savedInstanceState)
//        AppCompatDelegate.setDefaultNightMode(if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        println("dawlgdkalwj " + binding)
        binding = inflate(layoutInflater)
        println("dawlgdkalwj " + binding)
        setContentView(binding?.root)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        setupUI(parent)
        return super.onCreateView(parent, name, context, attrs)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

//    fun getErrorDialog(
//        title: String,
//        message: String,
//        btnText: String? = getString(R.string.ok),
//        onButtonClick: () -> Unit? = {}
//    ): Dialog {
//        val d = Dialog(this, R.style.CustomDialog)
//        d.setContentView(R.layout.custom_error_dialog)
//        val dialogTitle = d.findViewById<TextView>(R.id.dialog_title)
//        val dialogMessage = d.findViewById<TextView>(R.id.dialog_message)
//        val dialogBtn = d.findViewById<TextView>(R.id.dialog_btn_ok)
//        dialogTitle.text = title
//        dialogMessage.text = message
//        dialogBtn.text = btnText
//        dialogBtn.setOnClickListener { onButtonClick.invoke(); d.dismiss() }
//        return d
//    }
//
//
//    fun getErrorDialog(
//        title: String,
//        message: String,
//        btnText: String? = getString(R.string.ok),
//        onButtonClick: () -> Unit? = {},
//        linkIsMustBeSelected : Boolean = false
//    ): Dialog {
//        val d = Dialog(this, R.style.CustomDialog)
//        d.setContentView(R.layout.custom_error_dialog)
//        val dialogTitle = d.findViewById<TextView>(R.id.dialog_title)
//        val dialogMessage = d.findViewById<TextView>(R.id.dialog_message)
//        val dialogBtn = d.findViewById<TextView>(R.id.dialog_btn_ok)
//        dialogTitle.text = title
//        if(linkIsMustBeSelected) dialogMessage.setTextViewTextAsLink(message)
//        else dialogMessage.text = message
//        dialogBtn.text = btnText
//        dialogBtn.setOnClickListener { onButtonClick.invoke(); d.dismiss() }
//        return d
//    }
//
//    fun getAlertDialog(
//        title: String,
//        btnTextYes: String?,
//        btnTextNo: String?,
//        onButtonClickYes: () -> Unit? = {},
//        onButtonClickNo: () -> Unit? = {}
//    ): Dialog {
//        val d = Dialog(this, R.style.CustomDialog)
//        d.setContentView(R.layout.custom_alert_dialog)
//        val dialogTitle = d.findViewById<TextView>(R.id.dialog_title)
//        val dialogBtnYes = d.findViewById<TextView>(R.id.dialog_btn_yes)
//        val dialogBtnNo = d.findViewById<TextView>(R.id.dialog_btn_no)
//        dialogTitle.text = title
//        dialogBtnYes.text = btnTextYes
//        dialogBtnNo.text = btnTextNo
//        dialogBtnYes.setOnClickListener { onButtonClickYes.invoke(); d.dismiss() }
//        dialogBtnNo.setOnClickListener { onButtonClickNo.invoke(); d.dismiss() }
//        return d
//    }
//
//    fun getAlertDialogWithEditText(
//        title: String,
//        btnTextYes: String?,
//        btnTextNo: String?,
//        editTextDefaultText: String = "",
//        onButtonClickYes: (text: String) -> Unit? = {},
//        onButtonClickNo: () -> Unit? = {},
//    ): Dialog {
//        val d = Dialog(this, R.style.CustomDialog)
//        d.setContentView(R.layout.custom_edit_alert_dialog)
//        val dialogTitle = d.findViewById<TextView>(R.id.dialog_title)
//        val dialogBtnYes = d.findViewById<TextView>(R.id.dialog_btn_yes)
//        val dialogBtnNo = d.findViewById<TextView>(R.id.dialog_btn_no)
//        val editText = d.findViewById<TextView>(R.id.editText)
//        editText.text = editTextDefaultText
//        dialogTitle.text = title
//        dialogBtnYes.text = btnTextYes
//        dialogBtnNo.text = btnTextNo
//        dialogBtnYes.setOnClickListener { onButtonClickYes.invoke(editText.text.toString()); d.dismiss() }
//        dialogBtnNo.setOnClickListener { onButtonClickNo.invoke(); d.dismiss() }
//        return d
//    }
//
//    fun getAlertDialogWithPicker(
//        title: String,
//        btnTextYes: String?,
//        btnTextNo: String?,
//        listOfPicker: List<String>,
//        onButtonClickYes: (text: String) -> Unit? = {},
//        onButtonClickNo: () -> Unit? = {},
//    ): Dialog {
//        val d = Dialog(this, R.style.CustomDialog)
//        d.setContentView(R.layout.custom_picker_alert_dialog)
//        val dialogTitle = d.findViewById<TextView>(R.id.dialog_title)
//        val dialogBtnYes = d.findViewById<TextView>(R.id.dialog_btn_yes)
//        val dialogBtnNo = d.findViewById<TextView>(R.id.dialog_btn_no)
//        val picker = d.findViewById<NumberPicker>(R.id.picker)
//        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS)
//        picker.minValue = 0
//        picker.maxValue = listOfPicker.size - 1
//        picker.displayedValues = listOfPicker.toTypedArray()
//        dialogTitle.text = title
//        dialogBtnYes.text = btnTextYes
//        dialogBtnNo.text = btnTextNo
//        dialogBtnYes.setOnClickListener { onButtonClickYes.invoke(listOfPicker[picker.value]); d.dismiss() }
//        dialogBtnNo.setOnClickListener { onButtonClickNo.invoke(); d.dismiss() }
//        return d
//    }
}
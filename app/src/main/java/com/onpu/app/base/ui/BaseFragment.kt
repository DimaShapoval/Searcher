package com.onpu.app.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.hannesdorfmann.fragmentargs.FragmentArgs
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.onpu.app.util.setupUI

@FragmentWithArgs
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected var binding: VB? = null

    protected abstract fun inflate(
        inflate: LayoutInflater, viewGroup: ViewGroup?, attachToRoot: Boolean = false
    ): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentArgs.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, container)
        activity?.setupUI(binding?.root)
        return binding?.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

//    fun getErrorDialog(
//        title: String,
//        message: String,
//        btnText: String? = getString(R.string.ok),
//        onButtonClick: () -> Unit? = {},
//        onLinkClickListener : (text : String) -> Unit? = {}
//    ): Dialog {
//        val d = Dialog(requireContext(), R.style.CustomDialog)
//        d.setContentView(R.layout.custom_error_dialog)
//        val dialogTitle = d.findViewById<TextView>(R.id.dialog_title)
//        val dialogMessage = d.findViewById<TextView>(R.id.dialog_message)
//        val dialogBtn = d.findViewById<TextView>(R.id.dialog_btn_ok)
//        dialogTitle.text = title
//        dialogMessage.setTextViewTextAsLink(message, onLinkClickListener)
//        dialogBtn.text = btnText
//        dialogBtn.setOnClickListener { onButtonClick.invoke(); d.dismiss() }
//        return d
//    }
//
//    fun Context.getErrorDialog(
//        title: String,
//        message: String,
//        btnText: String = getString(R.string.ok),
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
//    fun Context.getErrorDialog(): Dialog {
//        val d = Dialog(this, R.style.CustomDialog)
//        d.setContentView(R.layout.custom_error_dialog)
//        val dialogBtn = d.findViewById<TextView>(R.id.dialog_btn_ok)
//        dialogBtn.onClickListener { d.dismiss() }
//        return d
//    }
}
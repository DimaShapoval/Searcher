package com.onpu.app.base.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onpu.domain.mvp.base.dialog.BaseBottomDialogMvp.BaseDialog
import com.onpu.domain.mvp.base.dialog.BaseBottomDialogMvp.BasePresenter


abstract class BaseBottomSheetDialogMvp<P : BasePresenter<D>, D : BaseDialog, VB: ViewBinding>(
    context: Context,
    protected val presenter: P
) : BottomSheetDialog(context), BaseDialog {

    protected var binding: VB? = null
    abstract val TAG: String

    protected abstract fun inflate(
        inflate: LayoutInflater
    ): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater).apply { setContentView(root) }
        presenter.attachDialog(this as D)
        presenter.onCreatedDialog()
    }

    override fun dismiss() {
        presenter.detachDialog()
        binding = null
        super.dismiss()
    }

    override fun expandBottomSheet() {
        behavior.state = STATE_EXPANDED
    }

    override val noDimBehind: Unit?
        get() = window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
}

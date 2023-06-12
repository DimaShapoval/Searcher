package com.onpu.domain.mvp.base.dialog

interface BaseBottomDialogMvp {

    interface BaseDialog {
        fun dismiss()
        fun show()
        fun expandBottomSheet()
        val noDimBehind: Unit?
    }

    interface BasePresenter<D : BaseDialog> {
        var dialog: D?
        fun attachDialog(dialog: D)
        fun detachDialog(): Unit?
        fun onCreatedDialog(): Unit?
    }
}
package com.onpu.domain.mvp.sample

import com.onpu.domain.mvp.adapter.SampleAdapterMvp
import com.onpu.domain.mvp.base.ui.BaseMvp

interface SampleMvp {
    interface View : BaseMvp.BaseView {
        fun onAdapterCreated(presenter: SampleAdapterMvp.Presenter): Unit?
        fun initListeners()
    }

    interface Presenter : BaseMvp.BasePresenter<View> {
        fun onFindTextChanged(findText: String)
    }
}
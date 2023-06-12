package com.onpu.domain.mvp.ui.main_screen

import com.onpu.domain.mvp.base.ui.BaseMvp

interface MainMvp {
    interface View : BaseMvp.BaseView {
        fun showError()
        fun generalSearch(url: String)
        fun showNavigationView(text: String)
        fun hideNavigationView()
        fun showSavedLink(link : String): Unit?
    }

    interface Presenter : BaseMvp.BasePresenter<View> {
        fun searchSiteByLink(text: String)
        fun getLastLink():String
    }
}
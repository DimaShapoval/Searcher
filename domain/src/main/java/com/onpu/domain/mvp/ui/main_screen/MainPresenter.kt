package com.onpu.domain.mvp.ui.main_screen

import com.onpu.domain.model.SpecifiersEnums
import com.onpu.domain.mvp.base.ui.BasePresenterImpl

class MainPresenter() : BasePresenterImpl<MainMvp.View>(), MainMvp.Presenter {
    override fun onCreate() {
        view?.showSavedLink(preferences.lastLinkWithSpecifier)
    }

    override fun searchSiteByLink(text: String) {
        when {
            text.isNotValidURL() -> view?.showError()
            text.contains(SpecifiersEnums.INT.name) -> {
                preferences.lastLinkWithSpecifier = text
                view?.showNavigationView(text)
            }
            else -> {
                preferences.lastLinkWithSpecifier = text
                view?.hideNavigationView()
                view?.generalSearch(text)
            }
        }
    }

    override fun getLastLink():String = preferences.lastLinkWithSpecifier

    private fun String.isValidURL(): Boolean {
        val regex = Regex("^(http(s)?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ;,./?%&=]*)?\$")
        return regex.matches(this)
    }

    private fun String.isNotValidURL(): Boolean = !this.isValidURL()

}
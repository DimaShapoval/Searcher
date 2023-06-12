package com.onpu.domain.mvp.base.ui

interface BaseMvp {

    interface BaseView {
        fun onBackPressed()
        fun showErrorToast(message: String)
//        fun addLogAboutSuccess(text : String)
//        fun addLogAboutError(e : Throwable)
//        fun addLogAboutSystemInfo()
//        fun showErrorSnackbar()
//        fun showConnectionErrorSnackbar()
//        fun showErrorScreen()
//        fun hideErrorScreen()
//        fun showConnectionErrorScreen()
//        fun hideConnectionErrorScreen()

    }

    interface BasePresenter<V : BaseView> {
        fun attachView(view: V)
        fun detachView()
        fun onCreate(): Unit?
        fun onStart(): Unit?
        fun onStop(): Unit?
        fun onRestart(): Unit?
        fun onResume(): Unit?
        fun onPause(): Unit?
        fun reload()
        fun retryConnection()
        fun returnBack()
    }
}
package com.onpu.domain.mvp.base.service

interface BaseServiceMvp {

    interface BaseService

    interface BasePresenter<S : BaseService> {
        fun attachService(service: S)
        fun detachService()
        fun onCreate(): Unit?
        fun onStart(): Unit?
        fun onStop(): Unit?
    }
}
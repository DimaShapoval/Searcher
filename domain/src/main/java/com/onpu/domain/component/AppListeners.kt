package com.onpu.domain.component

interface AppListeners {
    var onUpdatedNightMode: ((isNightMode: Boolean) -> Unit)?

    fun subscribeUpdatedNightMode(onUpdatedNightMode: (onUpdatedNightMode: Boolean) -> Unit)
    fun unsubscribeUpdatedNightMode()
}
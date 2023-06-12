package com.onpu.core

import com.onpu.domain.component.AppListeners

class AppListenersImpl : AppListeners {

    override var onUpdatedNightMode: ((isNightMode: Boolean) -> Unit)? = null

    override fun subscribeUpdatedNightMode(onUpdatedNightMode: (isNightMode: Boolean) -> Unit) {
        this.onUpdatedNightMode = onUpdatedNightMode
    }

    override fun unsubscribeUpdatedNightMode() {
        onUpdatedNightMode = null
    }



}
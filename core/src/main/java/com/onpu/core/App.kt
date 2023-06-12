package com.onpu.core

import android.app.Application
import com.onpu.domain.component.ComponentProvider

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ComponentProvider.inject(ComponentProviderImpl(this))
    }
}
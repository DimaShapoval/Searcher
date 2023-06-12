package com.onpu.domain.component

import com.google.gson.Gson
import com.onpu.domain.api.HostSelectionInterceptor

interface ComponentProvider {

    val appSchedulers: AppSchedulers
    val preferences: Preferences
    val apiBaseUrl: String
    val dataRepository: DataRepository
    val gson: Gson
    val appListeners: AppListeners
    val useCases: UseCases
    val hashQuery: HashQuery
    val timer: Timer
//    val appsListDisposable: AppsList
    val apiErrorUrl: String
//    val pathToNetworkLogFile: String
    val hostInterceptor: HostSelectionInterceptor

    companion object {
        lateinit var instance: ComponentProvider private set

        fun inject(instance: ComponentProvider) = synchronized(this) {
            Companion.instance = instance
        }
    }

}

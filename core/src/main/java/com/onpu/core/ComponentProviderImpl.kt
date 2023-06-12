package com.onpu.core

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.google.gson.Gson
import com.onpu.core.data.DataRepositoryImpl
import com.onpu.core.data.HashQueryImpl
import com.onpu.core.data.Mapper
import com.onpu.core.data.local.PreferencesImpl
import com.onpu.core.data.use_case.UseCasesImpl
import com.onpu.core.scheduler.AppSchedulersImpl
import com.onpu.domain.api.HostSelectionInterceptor
import com.onpu.domain.component.*
import io.reactivex.plugins.RxJavaPlugins

class ComponentProviderImpl(private val context: Context) : ComponentProvider {

    override val preferences: Preferences by lazy { PreferencesImpl(context, appListeners) }
    override val dataRepository: DataRepository by lazy {
        DataRepositoryImpl(appSchedulers, preferences, gson)
    }

    override val appSchedulers: AppSchedulers by lazy { AppSchedulersImpl() }
    override val appListeners: AppListeners by lazy { AppListenersImpl() }
    override val apiBaseUrl by lazy { "https://example.com/" }

    override val gson: Gson get() = Mapper().gson()
    override val hashQuery: HashQuery by lazy { HashQueryImpl() }
    override val useCases: UseCases by lazy { UseCasesImpl(context) }
    override val timer: Timer by lazy { TimerImpl(appSchedulers) }
    private val packageManager: PackageManager by lazy { context.packageManager }

    private val PackageInfo.appName
        get() = applicationInfo.loadLabel(packageManager).toString()

    val ApplicationInfo.getNameApp
        get() = packageManager.getPackageInfo(
            this.packageName,
            PackageManager.GET_META_DATA
        ).appName

    val packages: MutableList<ApplicationInfo>
        get() = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

//    override val appsListDisposable: AppsList by lazy { AppsListImpl(appSchedulers, packages) }
    override val apiErrorUrl: String = "https://example.com/"
//    override val pathToNetworkLogFile: String =
//        context.filesDir.path + "/" + BuildConfig.LOG_FILE_NAME
    override val hostInterceptor: HostSelectionInterceptor by lazy { HostSelectionInterceptor() }

    init {
        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
        }
    }
}

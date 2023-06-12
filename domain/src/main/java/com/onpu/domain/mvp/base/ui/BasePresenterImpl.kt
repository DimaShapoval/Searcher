package com.onpu.domain.mvp.base.ui

import com.onpu.domain.component.ComponentProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BasePresenterImpl<V : BaseMvp.BaseView> : BaseMvp.BasePresenter<V> {

    protected val view: V? get() = viewRef?.get()

    private var viewRef: WeakReference<V>? = null
    protected val subscriptions = CompositeDisposable()
    protected val appListeners get() = componentProvider.appListeners
    protected val componentProvider = ComponentProvider.instance
    protected val schedulers get() = componentProvider.appSchedulers
    protected val preferences = componentProvider.preferences
    protected val dataRepository by lazy { componentProvider.dataRepository }
    protected val useCases by lazy { componentProvider.useCases }
    protected val hashQuery by lazy { componentProvider.hashQuery }
//    protected val appsList get() = componentProvider.appsListDisposable
    protected val timer by lazy { componentProvider.timer }
    private var isStopped = false
    protected var isLoading = false
        set(value) {
            field = value
            if (value) {
                obtainView()
            } else {
                obtainView()
            }
        }

    protected fun subscriptions(add: () -> Disposable) = subscriptions.add(add()).run { }
    protected var requiresLocation = false

    protected fun <T> Single<T>.subscribeUi(
        onSuccess: (T) -> Unit, onError: (Throwable) -> Unit = { it.printStackTrace() }
    ) = subscriptions {
        observeOn(schedulers.ui).subscribe(
            {
//                view?.addLogAboutSuccess(it.toString())
                onSuccess.invoke(it)
            },
            {
//                view?.addLogAboutError(it)
                onError.invoke(it)
            }
        )

    }

    protected fun Completable.subscribeUi(
        onComplete: () -> Unit, onError: (Throwable) -> Unit = { it.printStackTrace() }
    ) = subscriptions {
        observeOn(schedulers.ui).subscribe({
//                view?.addLogAboutSuccess()
            onComplete.invoke()
        }, {
//                view?.addLogAboutError(it)
            onError.invoke(it)
        })
    }

    protected fun <T> Observable<T>.subscribeUi(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = { it.printStackTrace() },
        onComplete: () -> Unit = { }
    ) = subscriptions {
        observeOn(schedulers.ui).subscribe(
            {
//                view?.addLogAboutSuccess(it.toString())
                onNext.invoke(it)
            }, {
//                view?.addLogAboutError(it)
                onError.invoke(it)
            },
            onComplete
        )
    }


    override fun attachView(view: V) {
        println("attachView " + view)
        viewRef = WeakReference(view)
    }

    override fun detachView() {
        viewRef?.clear(); subscriptions.clear()
    }

    override fun onStart() {
        isStopped = false
    }

    override fun onStop() {
        isStopped = true
    }

    override fun onRestart(): Unit? = Unit
    override fun onResume(): Unit? = Unit
    override fun onPause(): Unit? = Unit

    protected fun obtainView(): V? = if (isStopped) null else view

    protected fun isConnectionError(t: Throwable): Boolean {
        return t is UnknownHostException || t is ConnectException || t is SocketTimeoutException
    }

    override fun reload() {}

    override fun retryConnection() {}

    override fun returnBack() {
        obtainView()?.onBackPressed()
    }

}

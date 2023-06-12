package com.onpu.domain.mvp.base.ui

import com.onpu.domain.component.ComponentProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseDialogPresenter {

    protected val subscriptions = CompositeDisposable()
    private val componentProvider = ComponentProvider.instance
    protected val schedulers get() = componentProvider.appSchedulers
    protected val preferences = componentProvider.preferences
    protected val dataRepository by lazy { componentProvider.dataRepository }
    //    protected val firebase by lazy { componentProvider.firebaseImpl }
    protected val appListeners get() = componentProvider.appListeners

    protected fun subscriptions(add: () -> Disposable) = subscriptions.add(add()).run {  }

    protected fun<T> Single<T>.subscribeUi(
        onSuccess: (T) -> Unit, onError: (Throwable) -> Unit = { it.printStackTrace() }
    ) = subscriptions { observeOn(schedulers.ui).subscribe(onSuccess, onError) }

    protected fun<T> Observable<T>.subscribeUi(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = { it.printStackTrace() },
        onComplete: () -> Unit = {  }
    ) = subscriptions { observeOn(schedulers.ui).subscribe(onSuccess, onError, onComplete) }

}
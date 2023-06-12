package com.onpu.core

import com.onpu.domain.component.AppSchedulers
import com.onpu.domain.component.Timer
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class TimerImpl(private val schedulers: AppSchedulers): Timer {

    private val disposable by lazy { CompositeDisposable() }

    override fun subscribe(time: Int, doOnNext: (time:Long) -> Unit?,doOnEnd: () -> Unit, onError: () -> Unit) {
        if (disposable.size() == 0) disposable.add(timer(time, doOnNext ,doOnEnd, onError))
    }

    private fun timer(time: Int, doOnNext: (time:Long) -> Unit?, doOnEnd: () -> Unit, onError: () -> Unit) : Disposable {
        var timer = time
        return Observable
            .intervalRange(0, timer.toLong(), 0, 1, TimeUnit.SECONDS, schedulers.io)
            .observeOn(schedulers.ui)
            .subscribe(
                {
                    --timer
                    doOnNext.invoke(it)
                },
                { onError() },
                { doOnEnd() }

                //он скореее всего работает не так как ты думал
                //ароче тебе осталосзсунут ь в диспосабл, и отписыватсья когда закончил за  ним следит
//            ибо будет в мпамяти
//            както +- так
            )
    }

    override fun unsubscribe() {
        disposable.clear()
    }
}
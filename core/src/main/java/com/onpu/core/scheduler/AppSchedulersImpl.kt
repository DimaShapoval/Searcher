package com.onpu.core.scheduler

import com.onpu.domain.component.AppSchedulers
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class AppSchedulersImpl : AppSchedulers {
    override val io: Scheduler = Schedulers.from(Executors.newFixedThreadPool(5))
    override val ui: Scheduler get() = AndroidSchedulers.mainThread()
    override val db: Scheduler = Schedulers.from(Executors.newSingleThreadExecutor())
}

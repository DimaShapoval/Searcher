package com.onpu.domain.component

import io.reactivex.Scheduler

interface AppSchedulers {
    val io: Scheduler
    val ui: Scheduler
    val db: Scheduler
}

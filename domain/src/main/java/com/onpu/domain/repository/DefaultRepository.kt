package com.onpu.domain.repository

import io.reactivex.Single

interface DefaultRepository<B, T> {
    fun load(params: B): Single<T>
}
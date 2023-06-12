package com.onpu.domain.data.repository

import io.reactivex.Single

interface Repository<T> {
    val loadData: Single<List<T>>
}
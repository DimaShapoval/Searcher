package com.onpu.domain.data.repository

import io.reactivex.Single

interface LocalRepository<T> : Repository<T> {
//    fun getDataByKey(key: String): Single<T>
    fun insertData(data: List<T>): Single<Boolean>
    fun insertData(data: T): Single<Boolean>
    fun insertData(data: Map<String, String>): Single<Boolean>
//    fun<V> insertDataToBD(iterableItem : HashMap<String,V>)
    fun updateData(data: T): Single<Boolean>
    fun updateData(data: List<T>): Single<Boolean>
    fun removeData(data: T): Single<Boolean>
    fun removeData(data: List<T>): Single<Boolean>
    val clearDatabase : Single<Unit>
    val clearMemoryCache: Single<Unit>
}
package com.onpu.core.data

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.onpu.domain.api.ClientApiMain
import com.onpu.domain.component.AppSchedulers
import com.onpu.domain.component.DataRepository
import com.onpu.domain.component.Preferences
import com.onpu.domain.repository.DefaultRepository
import com.onpu.domain.repository.DefaultRepositoryImpl

class DataRepositoryImpl(
    private val schedulers: AppSchedulers,
    private val preferences: Preferences,
    private val gson: Gson,
) : DataRepository {

    override val dataFromServer: DefaultRepository<Map<String, String>, JsonElement>
        get() = DefaultRepositoryImpl(schedulers, gson, ClientApiMain::postMainInfo)


}
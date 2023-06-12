package com.onpu.domain.component

import com.google.gson.JsonElement
import com.onpu.domain.repository.DefaultRepository

interface DataRepository {

    val dataFromServer: DefaultRepository<Map<String, String>, JsonElement>

}
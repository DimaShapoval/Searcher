package com.onpu.domain.repository

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.onpu.domain.api.Api
import com.onpu.domain.api.ClientApiMain
import com.onpu.domain.component.AppSchedulers
import com.onpu.domain.data.error.ServerError
import io.reactivex.Single

class DefaultRepositoryImpl(
    private val schedulers: AppSchedulers,
    private val gson: Gson,
    private val loadData: ClientApiMain.(Map<String, String>) -> Single<JsonElement>
) : DefaultRepository<Map<String, String>, JsonElement> {

    override fun load(params: Map<String, String>): Single<JsonElement> {
        if (params["request_sign"] == null) return Single.error(Throwable("request_sign is required"))
        return loadData(Api.client, params)
            .subscribeOn(schedulers.io)
            .map(::checkResult)
    }

    private fun checkResult(it: JsonElement): JsonElement {
        if (!it.isJsonObject) throw Throwable("response is not object")
        val json = it.asJsonObject
        var temp: JsonElement? = json.get("status")
        val status = if (temp != null && temp.isJsonPrimitive) temp.asString else ""
        temp = json.get("response")
        return when {
            temp != null && status.isOk() -> temp
            temp != null && !status.isOk() -> throw readError(temp)
            !status.isOk() -> throw Throwable("response error is null")
            temp == null && status.isOk() -> json
            else -> throw Throwable("result is null")
        }
    }

    private fun readError(item: JsonElement): Throwable {
        if (!item.isJsonObject) return Throwable("response error is not object")
        val json = item.asJsonObject
        var temp: JsonElement? = json.get("error_message")
        val message = if (temp == null || temp.isJsonNull) "" else temp.asString

        temp = json.get("error_type")
        val errorType = if (temp == null || temp.isJsonNull) "" else temp.asString

        temp = json.get("error_code")
        val code = if (temp == null || temp.isJsonNull) null else gson.fromJson(
            temp,
            ServerError.Code::class.java
        )
        return ServerError(message, typeError = errorType)
    }

    private fun String.isOk() = equals("ok", true)

}
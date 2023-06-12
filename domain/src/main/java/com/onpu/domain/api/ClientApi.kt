package com.onpu.domain.api

import com.google.gson.JsonElement
import io.reactivex.Single
import retrofit2.http.*

interface ClientApi {

    @FormUrlEncoded
    @POST("/v1/")
    fun postMainInfo(
        @FieldMap (encoded = true) params: Map<String, String>,
    ): Single<JsonElement>

}
package com.onpu.domain.data.error

import com.google.gson.annotations.SerializedName

class BaseResponseError(
    @SerializedName("error_message") val errorMessage: String?,
    @SerializedName("error_code") val errorCode: ServerError.Code?
) {
}
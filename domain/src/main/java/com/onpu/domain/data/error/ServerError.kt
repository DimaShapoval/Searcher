package com.onpu.domain.data.error

import com.google.gson.annotations.SerializedName

class ServerError(
    override val message: String? = null,
    val numberOfError: Code? = null,
    val typeError: String? = null,
) : Throwable(message) {


    enum class Code {

        @SerializedName("1") ERROR1,
        @SerializedName("2") ERROR2,
        @SerializedName("3") ERROR3,
        @SerializedName("4") ERROR4,
        @SerializedName("5") ERROR5,
        @SerializedName("6") ERROR6,
        @SerializedName("7") ERROR7,
        @SerializedName("8") ERROR8,
        @SerializedName("9") ERROR9,
        @SerializedName("10") ERROR10,
        @SerializedName("11") ERROR11,
        @SerializedName("12") ERROR12,
        @SerializedName("13") ERROR13,
        @SerializedName("14") ERROR14,
        @SerializedName("15") ERROR15
    }
}
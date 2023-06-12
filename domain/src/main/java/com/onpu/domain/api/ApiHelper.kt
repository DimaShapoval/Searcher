package com.onpu.domain.api

import com.google.gson.JsonObject


//fun postLogin(email: String, password: String) = json {
//    addProperty("email", email)
//    addProperty("password", password)
//}

private fun json(apply: JsonObject.() -> Unit) = JsonObject().apply(apply)
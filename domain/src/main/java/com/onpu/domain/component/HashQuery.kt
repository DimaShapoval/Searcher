package com.onpu.domain.component

interface HashQuery {
    fun paramsToHash(vararg param: Pair<String, String>, isErrorServer:Boolean): String
    fun paramsToHash(param: Map<String, String>, isErrorServer:Boolean): String
}
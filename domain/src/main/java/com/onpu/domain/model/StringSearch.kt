package com.onpu.domain.model

data class StringSearch(
    val key : String,
    val value : String,
    val indexStartText : Int,
    val indexEndText : Int
){
    var specifierValue : Int = 0
}
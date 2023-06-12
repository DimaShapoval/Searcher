package com.onpu.domain.component

interface Timer {
    fun subscribe(time: Int, doOnNext : (time:Long) -> Unit?, doOnEnd: () -> Unit, onError: () -> Unit = {})
    fun unsubscribe()
}
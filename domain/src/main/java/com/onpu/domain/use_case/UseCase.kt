package com.onpu.domain.use_case

interface UseCase<R : Any> {
    fun perform(): R
}
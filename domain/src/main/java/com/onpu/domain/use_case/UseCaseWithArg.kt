package com.onpu.domain.use_case

import com.onpu.domain.data.args.BaseArg

interface UseCaseWithArg<B : BaseArg, R : Any> {
    fun perform(arg: B): R
}
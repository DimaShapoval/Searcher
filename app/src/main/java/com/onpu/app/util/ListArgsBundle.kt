package com.onpu.app.util

import android.os.Bundle
import com.hannesdorfmann.fragmentargs.bundler.ArgsBundler
import java.io.Serializable

class ListArgsBundle<T> : ArgsBundler<MutableList<T>> {

    override fun put(key: String, value: MutableList<T>, bundle: Bundle) {
        bundle.putSerializable(key, value as Serializable)
    }

    override fun <V : MutableList<T>> get(key: String, bundle: Bundle): V {
        return bundle.getSerializable(key) as V
    }
}
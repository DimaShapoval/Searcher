package com.onpu.domain.api

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HostSelectionInterceptor : Interceptor {
    @Volatile
    private var host: HttpUrl? = null
    fun setHost(host: String) {
        var localHost = host
        if (!host.startsWith("https")){
            localHost = "https://$host"
        }
        this.host = localHost.toHttpUrlOrNull()
    }
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = host?.let {
            val newUrl = request.url.newBuilder()
                .scheme(it.scheme)
                .host(it.toUrl().toURI().host)
                .port(it.port)
                .build()
            return@let request.newBuilder()
                .url(newUrl)
                .build()
        }

        return  if (newRequest == null) chain.proceed(request.newBuilder().build()) else chain.proceed(newRequest)
    }
}

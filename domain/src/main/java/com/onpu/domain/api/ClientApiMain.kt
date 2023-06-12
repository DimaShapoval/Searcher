package com.onpu.domain.api

import com.google.gson.JsonElement
import com.onpu.domain.component.ComponentProvider
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ClientApiMain {

    private val mapper get() = ComponentProvider.instance.gson
    private var clientApi = provideApi()



    fun postMainInfo(params: Map<String, String>): Single<JsonElement> = clientApi
        .postMainInfo(params)

    private fun provideApi(): ClientApi {
        return Retrofit.Builder()
            .baseUrl(ComponentProvider.instance.apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(mapper))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ClientApi::class.java)
    }



    private fun provideHttpClient():OkHttpClient{
       val hostInterceptor = ComponentProvider.instance.hostInterceptor
       return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .addInterceptor(hostInterceptor)
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .retryOnConnectionFailure(true)
            .build()
    }

}
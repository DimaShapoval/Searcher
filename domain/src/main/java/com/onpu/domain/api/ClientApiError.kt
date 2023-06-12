package com.onpu.domain.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.onpu.domain.component.ComponentProvider
import com.onpu.domain.util.CalendarTypeAdapter
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ClientApiError {

    private val clientApi = provideApi()

    fun postMainInfo(params: Map<String, String>): Single<JsonElement> = clientApi
        .postMainInfo(params)

    private fun provideApi(): ClientApi {
        return Retrofit.Builder()
            .baseUrl(ComponentProvider.instance.apiErrorUrl)
            .client(provideHttpClient())
            .addConverterFactory(GsonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ClientApi::class.java)
    }

    private val objectMapper: Gson
        get() = GsonBuilder()
            .registerTypeAdapter(Calendar::class.java, CalendarTypeAdapter())
            .registerTypeAdapter(GregorianCalendar::class.java, CalendarTypeAdapter())
            .create()

    private fun provideHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//        .protocols(Collections.singletonList(Protocol.HTTP_1_1))
        .retryOnConnectionFailure(true)
        .build()

}

package com.ketworie.wheep.client.dao

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ketworie.wheep.client.MainApplication.Companion.SERVER_BASE
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton


@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideChatService(authInterceptor: AuthInterceptor): ChatService {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_BASE)
            .client(httpClient)
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
            .build()
        return retrofit.create(ChatService::class.java)
    }

}
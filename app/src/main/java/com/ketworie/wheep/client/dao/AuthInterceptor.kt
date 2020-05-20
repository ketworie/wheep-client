package com.ketworie.wheep.client.dao

import com.ketworie.wheep.client.MainApplication
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {

    var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (token == null)
            return chain.proceed(request)

        val newRequest = request
            .newBuilder()
            .addHeader(MainApplication.X_AUTH_TOKEN, token)
            .build()
        return chain.proceed(newRequest)
    }
}
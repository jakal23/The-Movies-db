package com.tell.moviedb.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response


class AuthenticatorInterceptor(private val apiKey: String): Interceptor {

    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Chain): Response {
        var request = chain.request()
        val url: HttpUrl = request.url.newBuilder()
            .addQueryParameter("api_key", apiKey).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }

}

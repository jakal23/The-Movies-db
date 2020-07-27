package com.tell.moviedb.network

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    val uri = Uri.Builder()
        .scheme(Constants.SCHEMA)
        .encodedAuthority(Constants.DOMAIN)


    val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
    val authenticator = AuthenticatorInterceptor(Constants.API_KEY)

    val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(authenticator)
        .build()

    val gson: Gson = GsonBuilder()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl("${uri.build()}/")
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


}
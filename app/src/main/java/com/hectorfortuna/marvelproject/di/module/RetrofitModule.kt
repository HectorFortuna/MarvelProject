package com.hectorfortuna.marvelproject.di.module

import com.google.gson.GsonBuilder
import com.hectorfortuna.marvelproject.util.baseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun initRetrofit(): com.hectorfortuna.marvelproject.data.network.Service {
        val gson = GsonBuilder().setLenient().create()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        return Retrofit.Builder()
            .baseUrl(baseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(com.hectorfortuna.marvelproject.data.network.Service::class.java)
    }
}
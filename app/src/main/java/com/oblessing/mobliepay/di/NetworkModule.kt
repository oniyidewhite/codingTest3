package com.oblessing.mobliepay.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oblessing.mobliepay.BuildConfig
import com.oblessing.mobliepay.core.EntityMapper
import com.oblessing.mobliepay.core.debug
import com.oblessing.mobliepay.model.PageResult
import com.oblessing.mobliepay.network.PlaceWebService
import com.oblessing.mobliepay.network.PlaceListEntity
import com.oblessing.mobliepay.network.PlaceEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)

        debug {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            okhttp.addNetworkInterceptor(logging)
        }

        return okhttp.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

    @Singleton
    @Provides
    fun provideMusicWebService(retrofit: Retrofit): PlaceWebService =
        retrofit.create(PlaceWebService::class.java)

    @Singleton
    @Provides
    fun provideMovieEntityMapper(mapper: PlaceEntityMapper): EntityMapper<PlaceListEntity?, PageResult?> =
        mapper
}
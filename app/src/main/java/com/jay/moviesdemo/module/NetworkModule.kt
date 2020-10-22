package com.jay.moviesdemo.module

import android.util.Log
import androidx.annotation.NonNull
import com.jay.moviesdemo.BuildConfig
import com.jay.moviesdemo.api.LiveDataCallAdapterFactory

import com.jay.moviesdemo.api.RequestInterceptor
import com.jay.moviesdemo.remote.MoviesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Module which provides all required dependencies about network
 */
@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            Log.d("Retrofit", message)
        })
        interceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(RequestInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(@NonNull okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    /**
     * Provides the Movie Service service implementation.
     * @param retrofit the Retrofit object
     * @return the Movie Service implementation.
     */
    @Provides
    @Singleton
    fun provideMovieService(@NonNull retrofit: Retrofit): MoviesService {
        return retrofit.create(MoviesService::class.java)
    }

}
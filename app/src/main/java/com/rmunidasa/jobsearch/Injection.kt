package com.rmunidasa.jobsearch

import com.rmunidasa.jobsearch.api.TemperApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Injection {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        jsonConverter: Moshi,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(TemperApiService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(jsonConverter))
            .build()

    @Provides
    @Singleton
    fun provideTemperApi(retrofit: Retrofit): TemperApiService =
        retrofit.create(TemperApiService::class.java)

}
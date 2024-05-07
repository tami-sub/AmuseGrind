package com.example.amusegrind.network

import com.example.amusegrind.core.utils.Constants
import com.example.amusegrind.network.data.YandexSpeechKitService
import com.example.amusegrind.network.utils.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkRemoteApiModule {
    @Singleton
    @Provides
    fun providesYandexSpeechKitRetrofitBuilder() : YandexSpeechKitService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.yandexSpeechKitBaseUrl)
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(YandexSpeechKitService::class.java)
    }
}
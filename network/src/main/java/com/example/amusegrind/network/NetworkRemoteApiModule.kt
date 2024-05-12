package com.example.amusegrind.network

import com.example.amusegrind.core.utils.Constants
import com.example.amusegrind.network.data.ImageApiService
import com.example.amusegrind.network.data.YandexSpeechKitService
import com.example.amusegrind.network.utils.FirePath
import com.example.amusegrind.network.utils.ResultCallAdapterFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
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

    @Singleton
    @Provides
    fun providesImageApiServiceRetrofitBuilder() : ImageApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.imageApiBaseUrl)
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ImageApiService::class.java)
    }

    @Provides
    @Singleton
    fun bindsFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = Firebase.database

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage

    @Provides
    @Singleton
    fun provideFirePath(): FirePath = FirePath
}
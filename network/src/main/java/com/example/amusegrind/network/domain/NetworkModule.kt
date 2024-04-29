package com.example.amusegrind.network.domain

import com.example.amusegrind.network.GoogleAuthServiceImpl
import com.example.amusegrind.network.VideosRepoImpl
import com.example.amusegrind.network.data.GoogleAuthService
import com.example.amusegrind.network.data.VideosRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    //    @Provides
//    fun provideUserRepository(apiService: ApiService): UserRepository = UserRetrofitRepository(apiService)
    @Binds
    fun bindGoogleAuthService(
        googleAuthServiceImpl: GoogleAuthServiceImpl
    ): GoogleAuthService

    @Binds
    fun bindVideosRepo(
        videosRepoImpl: VideosRepoImpl
    ): VideosRepo
}
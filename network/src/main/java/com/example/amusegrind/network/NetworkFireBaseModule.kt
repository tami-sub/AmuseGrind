package com.example.amusegrind.network

import com.example.amusegrind.network.data.AudiosRepo
import com.example.amusegrind.network.data.ChatRepo
import com.example.amusegrind.network.data.GoogleAuthService
import com.example.amusegrind.network.data.UserRepo
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkFireBaseModule {

    @Binds
    fun bindGoogleAuthService(
        googleAuthServiceImpl: GoogleAuthServiceImpl
    ): GoogleAuthService

    @Binds
    fun bindVideosRepo(
        audiosRepoImpl: AudiosRepoImpl
    ): AudiosRepo

    @Binds
    fun bindUsersRepo(
        userRepoImpl: UserRepoImpl
    ): UserRepo

    @Binds
    fun bindChatRepo(
        chatRepoImpl: ChatRepoImpl
    ): ChatRepo
}

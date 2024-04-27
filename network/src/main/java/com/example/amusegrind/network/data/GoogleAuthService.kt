package com.example.amusegrind.network.data

import android.content.Intent
import com.example.amusegrind.network.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface GoogleAuthService {
    fun getSignInIntent(): Intent
    fun handleSignInResult(data: Intent): Flow<Result<User>>

    fun getAccountInfo(): Flow<Result<User>>

    fun isUserAuthenticated(): Flow<Boolean>
}
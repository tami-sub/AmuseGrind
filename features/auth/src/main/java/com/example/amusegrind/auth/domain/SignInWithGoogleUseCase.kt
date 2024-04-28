package com.example.amusegrind.auth.domain

import android.content.Intent
import com.example.amusegrind.network.data.GoogleAuthService
import com.example.amusegrind.network.domain.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.random.Random

class SignInWithGoogleUseCase @Inject constructor(private val googleAuthService: GoogleAuthService) {
    fun getSignInIntent(): Intent {
        return googleAuthService.getSignInIntent()
    }

    fun handleSignInResult(data: Intent): Flow<Result<User>> {
        return googleAuthService.handleSignInResult(data)
    }

    operator fun invoke(): Flow<Boolean> {
        return googleAuthService.isUserAuthenticated()
    }
}
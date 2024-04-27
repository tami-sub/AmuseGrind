package com.example.amusegrind.auth.domain

import com.example.amusegrind.network.data.GoogleAuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import javax.inject.Inject
import kotlin.random.Random

class GetAuthStateUseCase @Inject constructor(private val googleAuthService: GoogleAuthService) {
    suspend fun invoke(): Flow<AuthState> {
        return flowOf(
            if (googleAuthService.isUserAuthenticated().first())
                AuthState.AUTHORIZED else AuthState.NOT_AUTHORIZED
        )
    }
}

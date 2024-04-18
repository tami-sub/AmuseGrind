package com.example.amusegrind.auth.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.random.Random

class GetAuthStateUseCase @Inject constructor() {
    fun invoke(): Flow<AuthState> {
        // TODO: implementation
        return flowOf(if (Random.nextBoolean()) AuthState.AUTHORIZED else AuthState.NOT_AUTHORIZED)
    }
}


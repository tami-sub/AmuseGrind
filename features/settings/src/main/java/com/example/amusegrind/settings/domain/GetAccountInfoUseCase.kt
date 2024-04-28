package com.example.amusegrind.settings.domain

import com.example.amusegrind.network.data.GoogleAuthService
import com.example.amusegrind.network.domain.entities.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountInfoUseCase @Inject constructor(private val googleAuthService: GoogleAuthService) {
    operator fun invoke(): Flow<Result<User>> {
        return googleAuthService.getAccountInfo()
    }
}
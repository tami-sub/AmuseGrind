package com.example.amusegrind.settings.domain

import com.example.amusegrind.network.data.UserRepo
import com.example.amusegrind.network.domain.entities.user.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountInfoUseCase @Inject constructor(
    private val userRepo: UserRepo,
) {
    suspend operator fun invoke(): Flow<Result<User?>> {
        return userRepo.getUserProfile(Firebase.auth.uid)
    }
}
package com.example.amusegrind.settings.domain

import com.example.amusegrind.network.data.GoogleAuthService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.random.Random

class SignOutWithGoogleUseCase @Inject constructor(private val googleAuthService: GoogleAuthService) {
    operator fun invoke() {
        googleAuthService.signOut()
    }
}
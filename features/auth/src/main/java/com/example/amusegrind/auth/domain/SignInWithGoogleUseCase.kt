package com.example.amusegrind.auth.domain

import android.content.Intent
import com.example.amusegrind.navigator.HomeDestination
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.network.data.GoogleAuthService
import com.example.amusegrind.network.data.UserRepo
import com.example.amusegrind.network.domain.entities.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val googleAuthService: GoogleAuthService,
    private val userRepo: UserRepo,
    private val navigator: Navigator
) {
    fun getSignInIntent(): Intent {
        return googleAuthService.getSignInIntent()
    }

    suspend fun handleSignInResult(data: Intent): Flow<User> {
        return flow {
            googleAuthService.handleSignInResult(data).first().onSuccess { pureUser ->
                if (!userRepo.isUserInRealtimeDatabase(pureUser.uid!!).first()) {
                    userRepo.addUserToDatabase(pureUser)
                }
                navigator.navigate(HomeDestination.route())
                emit(pureUser)
            }
        }
    }

    operator fun invoke(): Flow<Boolean> {
        return googleAuthService.isUserAuthenticated()
    }
}
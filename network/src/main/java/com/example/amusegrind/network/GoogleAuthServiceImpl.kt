package com.example.amusegrind.network

import android.content.Context
import android.content.Intent
import com.example.amusegrind.network.data.GoogleAuthService
import com.example.amusegrind.network.domain.entities.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleAuthServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GoogleAuthService {

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.client_id))
            .requestProfile()
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    // Method to get the sign-in intent
    override fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    // Method to handle the result from the sign-in intent
    override fun handleSignInResult(data: Intent): Flow<Result<User>> = flow {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = FirebaseAuth.getInstance().signInWithCredential(credential).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                emit(Result.success(User( uid = firebaseUser.uid, username = firebaseUser.displayName)))
            } else {
                throw FirebaseAuthException("auth/failed", "Firebase Auth failed")
            }
        } catch (e: ApiException) {
            emit(Result.failure(RuntimeException("Failed to complete Google sign-in", e)))
        }
    }

    override fun isUserAuthenticated(): Flow<Boolean> = flow {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val googleAccount = GoogleSignIn.getLastSignedInAccount(context)
        emit(firebaseUser != null && googleAccount != null)
    }

    override fun getAccountInfo(): Flow<Result<User>> = flow {
        try {
            val account = GoogleSignIn.getLastSignedInAccount(context)
            if (account != null) {
                val user = User(
                    uid = account.id ?: "",
                    username = account.displayName,
                    profilePictureUrl = account.photoUrl
                )
                emit(Result.success(user))
            } else {
                emit(Result.failure(RuntimeException("No Google account is currently signed in")))
            }
        } catch (e: Exception) {
            emit(Result.failure(RuntimeException("Failed to retrieve account information", e)))
        }
    }
}

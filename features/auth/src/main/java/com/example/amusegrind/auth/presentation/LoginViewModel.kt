package com.example.amusegrind.auth.presentation

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.auth.domain.AuthState
import com.example.amusegrind.auth.domain.GetAuthStateUseCase
import com.example.amusegrind.auth.domain.SignInWithGoogleUseCase
import com.example.amusegrind.auth.domain.SignOutWithGoogleUseCase
import com.example.amusegrind.navigator.Navigator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val signInWithGoogle: SignInWithGoogleUseCase,
    val signOutWithGoogleUseCase: SignOutWithGoogleUseCase,
    val getAuthStateUseCase: GetAuthStateUseCase,
    val navigator: Navigator
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState(auth = AuthState.NOT_AUTHORIZED))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d("joka", getAuthStateUseCase.invoke().first().name)
        }
    }

    fun handleGoogleSignInResult(data: Intent) {
        viewModelScope.launch {
            signInWithGoogle.handleSignInResult(data).collect { result ->
                result.onSuccess { user ->
                    Log.d("joka", user.username.toString())
                }.onFailure {
                    Log.d("joka", "F")
                }
            }
        }
    }

    fun getSignInIntent() = signInWithGoogle.getSignInIntent()


    fun signOut() = viewModelScope.launch {
        signOutWithGoogleUseCase.invoke()
    }
}
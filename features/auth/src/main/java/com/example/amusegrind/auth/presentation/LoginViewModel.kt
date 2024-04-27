package com.example.amusegrind.auth.presentation

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val navigator: Navigator,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState(name = ""))
    val state = _state.asStateFlow()

    fun handleGoogleSignInResult(data: Intent) {
        viewModelScope.launch {
            signInWithGoogle.handleSignInResult(data).collect { result ->
                result.onSuccess { user ->
                    _state.update {
                        it.copy(name = user.username.orEmpty())
                    }
                    Log.d("joka", "URA ${state.value.name}")
                }.onFailure {
                    Log.d("joka", "F")
                }
            }
        }
    }

    fun getSignInIntent() = signInWithGoogle.getSignInIntent()


    fun signOut() = viewModelScope.launch {
        signOutWithGoogleUseCase.invoke()
        Log.d("joka", signInWithGoogle.isUserAuthenticated().first().toString())
    }
}
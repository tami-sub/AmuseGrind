package com.example.amusegrind.auth.presentation

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.auth.domain.AuthState
import com.example.amusegrind.auth.domain.GetAuthStateUseCase
import com.example.amusegrind.auth.domain.SignInWithGoogleUseCase
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.navigator.SettingsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val signInWithGoogle: SignInWithGoogleUseCase,
    val getAuthStateUseCase: GetAuthStateUseCase,
    val navigator: Navigator
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState(auth = AuthState.NOT_AUTHORIZED))
    val state = _state.asStateFlow()

    fun handleGoogleSignInResult(data: Intent) {
        viewModelScope.launch {
            signInWithGoogle.handleSignInResult(data).collect { result ->
                result.onSuccess { user ->
                    Log.d("joka", user.username.toString())
                    navigator.navigate(SettingsDestination.route())
                }.onFailure {

                }
            }
        }
    }

    fun getSignInIntent() = signInWithGoogle.getSignInIntent()
}
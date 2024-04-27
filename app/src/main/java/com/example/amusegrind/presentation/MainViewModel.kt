package com.example.amusegrind.presentation

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.auth.domain.AuthState
import com.example.amusegrind.auth.domain.SignInWithGoogleUseCase
import com.example.amusegrind.auth.presentation.LoginState
import com.example.amusegrind.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val signInWithGoogle: SignInWithGoogleUseCase,
    val navigator: Navigator,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState(name = ""))
    val state = _state.asStateFlow()

    val showLoginScreen: Flow<Boolean> =
        signInWithGoogle.invoke().map { it }
}
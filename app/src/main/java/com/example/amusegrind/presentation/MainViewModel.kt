package com.example.amusegrind.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.auth.domain.AuthState
import com.example.amusegrind.auth.domain.GetAuthStateUseCase
import com.example.amusegrind.auth.domain.SignInWithGoogleUseCase
import com.example.amusegrind.auth.presentation.LoginState
import com.example.amusegrind.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val signInWithGoogle: SignInWithGoogleUseCase,
    val getAuthStateUseCase: GetAuthStateUseCase,
    val navigator: Navigator,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState(auth = AuthState.NOT_AUTHORIZED))
    val state = _state.asStateFlow()

    fun getAuthState() = viewModelScope.launch {
        _state.update { it.copy(auth = getAuthStateUseCase.invoke().first()) }
    }
}
package com.example.amusegrind.presentation

import androidx.lifecycle.ViewModel
import com.example.amusegrind.auth.domain.AuthState
import com.example.amusegrind.auth.domain.GetAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getAuthStateUseCase: GetAuthStateUseCase
) : ViewModel() {
    val showLoginScreen: Flow<Boolean> =
        getAuthStateUseCase.invoke().map { it == AuthState.NOT_AUTHORIZED }
}
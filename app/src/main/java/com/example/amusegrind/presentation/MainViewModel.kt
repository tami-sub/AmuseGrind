package com.example.amusegrind.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.auth.domain.AuthState
import com.example.amusegrind.auth.domain.GetAuthStateUseCase
import com.example.amusegrind.navigator.ChatDestination
import com.example.amusegrind.navigator.HomeDestination
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.navigator.RecordAudioDestination
import com.example.amusegrind.navigator.SettingsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private val _state = MutableStateFlow(MainState(auth = AuthState.NOT_AUTHORIZED))
    val state = _state.asStateFlow()

    init {
        getAuthState()
    }

    fun getAuthState() = viewModelScope.launch {
        _state.update {
            val authSate = getAuthStateUseCase.invoke().first()
            it.copy(
                auth = authSate,
                currentDestinationRoute = if (authSate == AuthState.AUTHORIZED) {
                    HomeDestination.route() // TODO заменить на HomeDestination.route()
                } else {
                    null
                }
            )
        }
    }

    fun navigateToHome() {
        state.value.currentDestinationRoute?.let {
            if (it != HomeDestination.route()) {
                navigator.replace(HomeDestination.route())
            }
        }
    }

    fun navigateToRecordAudio() {
        state.value.currentDestinationRoute?.let {
            if (it != RecordAudioDestination.route()) {
                navigator.replace(RecordAudioDestination.route())
            }
        }
    }

    fun navigateToChat() {
        state.value.currentDestinationRoute?.let {
            if (it != ChatDestination.route()) {
                navigator.replace(ChatDestination.route())
            }
        }
    }

    fun navigateToSettings() {
        state.value.currentDestinationRoute?.let {
            if (it != SettingsDestination.route()) {
                navigator.replace(SettingsDestination.route())
            }
        }
    }

    fun saveCurrentDestination(currentDestinationRoute: String) = _state.update {
        it.copy(currentDestinationRoute = currentDestinationRoute)
    }
}
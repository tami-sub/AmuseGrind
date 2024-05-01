package com.example.amusegrind.settings.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.navigator.LoginDestination
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.network.domain.entities.User
import com.example.amusegrind.settings.domain.GetAccountInfoUseCase
import com.example.amusegrind.settings.domain.SignOutWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signOutWithGoogleUseCase: SignOutWithGoogleUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState(user = User()))
    val state = _state.asStateFlow()

    fun getAccountInfo() = viewModelScope.launch {
        getAccountInfoUseCase.invoke().first().onSuccess { user ->
            user?.let {
                _state.update {
                    it.copy(user = user)
                }
            }
        }.onFailure {
            Log.d("joka", "Failed getAccountInfo")
        }
    }

    fun signOut() = viewModelScope.launch {
        signOutWithGoogleUseCase.invoke()
        navigator.replace(LoginDestination.route())
    }
}
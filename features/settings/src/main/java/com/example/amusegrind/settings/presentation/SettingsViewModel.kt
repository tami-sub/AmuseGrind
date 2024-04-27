package com.example.amusegrind.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.navigator.LoginDestination
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.settings.domain.SignOutWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val signOutWithGoogleUseCase: SignOutWithGoogleUseCase,
    val navigator: Navigator
) : ViewModel() {

    fun signOut() = viewModelScope.launch {
        signOutWithGoogleUseCase.invoke()
        navigator.replace(LoginDestination.route())
    }
}
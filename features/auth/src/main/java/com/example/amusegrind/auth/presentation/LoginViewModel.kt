package com.example.amusegrind.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.auth.domain.SignInWithGoogleUseCase
import com.example.amusegrind.navigator.Navigator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val signInWithGoogle: SignInWithGoogleUseCase,
    val navigator: Navigator,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState(name = ""))
    val state = _state.asStateFlow()

    fun signOut() = viewModelScope.launch {
        Log.d("joka", "singOut")
        Firebase.auth.signOut()
        signInWithGoogle.signOut()
        Log.d("joka", signInWithGoogle.isUserAuthenticated().first().toString())
    }
}
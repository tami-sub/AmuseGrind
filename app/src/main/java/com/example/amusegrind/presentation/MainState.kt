package com.example.amusegrind.presentation

import com.example.amusegrind.auth.domain.AuthState

data class MainState(val auth: AuthState, val currentDestinationRoute: String? = null)

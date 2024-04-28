package com.example.amusegrind.auth.presentation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.amusegrind.auth.R
import com.example.amusegrind.core.ui.AuthButton

@Composable
fun LoginScreen() {
    val viewModel: LoginViewModel = hiltViewModel()
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { viewModel.handleGoogleSignInResult(it) }
            }
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center
    ) {

        AuthButton(
            text = stringResource(id = R.string.sign_in),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            launcher.launch(viewModel.getSignInIntent())
        }
    }
}
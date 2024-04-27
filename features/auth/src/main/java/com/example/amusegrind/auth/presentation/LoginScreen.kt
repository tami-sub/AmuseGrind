package com.example.amusegrind.auth.presentation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen() {
    val viewModel: LoginViewModel = hiltViewModel()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White, CircleShape)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    launcher.launch(viewModel.getSignInIntent())
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "G",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }



        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White, CircleShape)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    viewModel.signOut()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "OUT",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }
    }
}
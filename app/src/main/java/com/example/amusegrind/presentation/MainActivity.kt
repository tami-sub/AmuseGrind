package com.example.amusegrind.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.ui.theme.AmuseGrindTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var navigator: Navigator

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmuseGrindTheme {
                MainScreen(navigator)
            }
        }

        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { viewModel.handleGoogleSignInResult(it) }
            }
        }

        val signInIntent = viewModel.getSignInIntent()

        googleSignInLauncher.launch(signInIntent)
    }
}

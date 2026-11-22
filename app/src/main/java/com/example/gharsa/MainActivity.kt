package com.example.gharsa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gharsa.ui.screens.DashboardScreen
import com.example.gharsa.ui.screens.LoginScreen
import com.example.gharsa.ui.theme.GharSaTheme
import com.example.gharsa.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GharSaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (authViewModel.isLoggedIn) {
                        DashboardScreen(viewModel = authViewModel)
                    } else {
                        LoginScreen(viewModel = authViewModel)
                    }
                }
            }
        }
    }
}

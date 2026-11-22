package com.example.gharsa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.gharsa.ui.screens.DashboardScreen
import com.example.gharsa.ui.screens.LoginScreen
import com.example.gharsa.ui.screens.MobileLoginScreen
import com.example.gharsa.ui.screens.ProfileScreen
import com.example.gharsa.ui.screens.SignUpScreen
import com.example.gharsa.ui.theme.GharSaTheme
import com.example.gharsa.viewmodel.AuthViewModel

enum class Screen {
    LOGIN, SIGN_UP, MOBILE_LOGIN, PROFILE, DASHBOARD
}

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
                    var currentScreen by remember { mutableStateOf(Screen.LOGIN) }

                    if (authViewModel.isLoggedIn) {
                        if (currentScreen == Screen.PROFILE) {
                            ProfileScreen(
                                viewModel = authViewModel,
                                onBack = { currentScreen = Screen.DASHBOARD }
                            )
                        } else {
                            DashboardScreen(
                                viewModel = authViewModel,
                                onNavigateToProfile = { currentScreen = Screen.PROFILE }
                            )
                        }
                    } else {
                        when (currentScreen) {
                            Screen.LOGIN -> LoginScreen(
                                viewModel = authViewModel,
                                onNavigateToSignUp = { currentScreen = Screen.SIGN_UP },
                                onNavigateToMobileLogin = { currentScreen = Screen.MOBILE_LOGIN }
                            )
                            Screen.SIGN_UP -> SignUpScreen(
                                viewModel = authViewModel,
                                onBackToLogin = { currentScreen = Screen.LOGIN }
                            )
                            Screen.MOBILE_LOGIN -> MobileLoginScreen(
                                viewModel = authViewModel,
                                onBackToEmailLogin = { currentScreen = Screen.LOGIN }
                            )
                            else -> LoginScreen(
                                viewModel = authViewModel,
                                onNavigateToSignUp = { currentScreen = Screen.SIGN_UP },
                                onNavigateToMobileLogin = { currentScreen = Screen.MOBILE_LOGIN }
                            )
                        }
                    }
                }
            }
        }
    }
}

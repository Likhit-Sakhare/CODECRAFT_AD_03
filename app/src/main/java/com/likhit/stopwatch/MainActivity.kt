package com.likhit.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.likhit.stopwatch.presentation.stopwatch.StopwatchScreenRoot
import com.likhit.stopwatch.ui.theme.StopwatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StopwatchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StopwatchScreenRoot(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
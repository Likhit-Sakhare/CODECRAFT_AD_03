package com.likhit.stopwatch

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.likhit.stopwatch.presentation.stopwatch.StopwatchScreenRoot
import com.likhit.stopwatch.ui.theme.StopwatchTheme
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissionToRequest = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if(arePermissionDenied()){
            requestPermission()
        }

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

    private fun arePermissionDenied(): Boolean{
        return permissionToRequest.any {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) != PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            permissionToRequest,
            1001
        )
    }
}
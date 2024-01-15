package com.example.detekt

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.detekt.presentation.CameraApp
import com.example.detekt.ui.theme.DeteKtTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkPermissions()){
            ActivityCompat.requestPermissions(
                this, permissions,0
            )
        }
        setContent {
            DeteKtTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraApp(parent = filesDir)
                }
            }
        }
    }
     private fun checkPermissions():Boolean{
        return permissions.all {
            ContextCompat.checkSelfPermission(
                applicationContext,it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object{
        private val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }
}


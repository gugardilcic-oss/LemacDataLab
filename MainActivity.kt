package com.example.lemacdatalab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.lemacdatalab.ui.navigation.AppNavigation
import com.example.lemacdatalab.ui.theme.LemacDataLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LemacDataLabTheme {
                AppNavigation()
            }
        }
    }
}

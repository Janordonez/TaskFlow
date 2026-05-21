package com.example.taskvmg2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.taskvmg2.ui.navigation.AppNavigation
import com.example.taskvmg2.ui.theme.TaskVMG2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskVMG2Theme {
                AppNavigation()
            }
        }
    }
}

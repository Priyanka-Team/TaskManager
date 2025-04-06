package com.bornfire.taskmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bornfire.taskmanagement.ui.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.rememberNavController
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavigation(navController)
        }
    }
}

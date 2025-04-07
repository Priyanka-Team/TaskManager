package com.bornfire.taskmanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bornfire.taskmanagement.ui.screen.HomeActivity
import com.bornfire.taskmanagement.ui.screen.TaskDetailActivity

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeActivity(navController) }
        composable("detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            TaskDetailActivity(navController, taskId)
        }
    }
}

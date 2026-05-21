package com.example.taskflow.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskflow.ui.screen.AddEditTaskScreen
import com.example.taskflow.ui.screen.SplashScreen
import com.example.taskflow.ui.screen.TaskListScreen
import com.example.taskflow.ui.viewmodel.TaskViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val taskViewModel: TaskViewModel = viewModel()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.SPLASH,
            modifier = modifier
        ) {
            composable(NavigationRoutes.SPLASH) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(NavigationRoutes.TASK_LIST) {
                            popUpTo(NavigationRoutes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            composable(NavigationRoutes.TASK_LIST) {
                TaskListScreen(
                    navController = navController,
                    viewModel = taskViewModel
                )
            }

            composable(
                route = "${NavigationRoutes.ADD_EDIT_TASK}?${NavigationRoutes.TASK_ID_ARG}={${NavigationRoutes.TASK_ID_ARG}}",
                arguments = listOf(
                    navArgument(NavigationRoutes.TASK_ID_ARG) {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments
                    ?.getInt(NavigationRoutes.TASK_ID_ARG)
                    ?.takeIf { it != -1 }

                AddEditTaskScreen(
                    navController = navController,
                    viewModel = taskViewModel,
                    taskId = taskId
                )
            }
        }
    }
}
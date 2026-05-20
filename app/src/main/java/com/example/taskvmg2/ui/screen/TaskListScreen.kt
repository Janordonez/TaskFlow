package com.example.taskvmg2.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.taskvmg2.ui.components.TaskCard
import com.example.taskvmg2.ui.navigation.NavigationRoutes
import com.example.taskvmg2.ui.viewmodel.TaskViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TaskListScreen(
    navController: NavController,
    viewModel: TaskViewModel
) {
    val tasks = viewModel.tasks.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavigationRoutes.addEditRoute()) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "TaskFlow",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = "Organizador de tareas academicas y personales",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            AnimatedVisibility(
                visible = tasks.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay tareas registradas. Usa el boton + para crear una.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-4).dp)
            ) {
                items(items = tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggleCompleted = { viewModel.toggleTaskCompletion(task.id) },
                        onDelete = { viewModel.deleteTask(task.id) },
                        onEdit = { navController.navigate(NavigationRoutes.addEditRoute(task.id)) },
                        onClick = { navController.navigate(NavigationRoutes.detailRoute(task.id)) }
                    )
                }
            }
        }
    }
}
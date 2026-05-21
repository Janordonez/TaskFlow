package com.example.taskvmg2.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.taskvmg2.ui.model.TaskPriority
import com.example.taskvmg2.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    navController: NavController,
    viewModel: TaskViewModel,
    taskId: Int?
) {
    val editingTask = remember(taskId) {
        taskId?.let { viewModel.getTaskById(it) }
    }

    var title by rememberSaveable(taskId) { mutableStateOf(editingTask?.title.orEmpty()) }
    var description by rememberSaveable(taskId) { mutableStateOf(editingTask?.description.orEmpty()) }
    var priority by rememberSaveable(taskId) { mutableStateOf(editingTask?.priority ?: TaskPriority.MEDIUM) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(if (taskId == null) "Nueva tarea" else "Editar tarea")
                }
            )
        }
    ) { innerPadding ->
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titulo") },
                    placeholder = { Text("Ej: Estudiar para parcial") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripcion") },
                    placeholder = { Text("Detalles o recordatorios") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )

                Text(
                    text = "Prioridad",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskPriority.entries.forEach { option ->
                        FilterChip(
                            selected = priority == option,
                            onClick = { priority = option },
                            label = {
                                Text(
                                    when (option) {
                                        TaskPriority.HIGH -> "Alta"
                                        TaskPriority.MEDIUM -> "Media"
                                        TaskPriority.LOW -> "Baja"
                                    }
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cancelar")
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (title.isBlank()) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("El titulo es obligatorio")
                                }
                                return@Button
                            }
                            viewModel.upsertTask(
                                taskId = taskId,
                                title = title,
                                description = description,
                                priority = priority
                            )
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar")
                    }
                }
            }
        }
    }

    LaunchedEffect(taskId) {
        if (taskId != null && editingTask == null) {
            snackbarHostState.showSnackbar("No se encontro la tarea a editar")
            navController.popBackStack()
        }
    }
}

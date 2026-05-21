package com.example.taskflow.ui.screen

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.taskflow.model.StatusFilter
import com.example.taskflow.model.TaskPriority
import com.example.taskflow.ui.navigation.NavigationRoutes
import com.example.taskflow.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    navController: NavController,
    viewModel: TaskViewModel,
    taskId: Int?
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val editingTask = taskId?.let { id -> uiState.tasks.firstOrNull { it.id == id } }

    var title by rememberSaveable(taskId) { mutableStateOf(editingTask?.title.orEmpty()) }
    var description by rememberSaveable(taskId) { mutableStateOf(editingTask?.description.orEmpty()) }
    var priority by rememberSaveable(taskId) { mutableStateOf(editingTask?.priority ?: TaskPriority.MEDIUM) }
    var status by rememberSaveable(taskId) { mutableStateOf(if (editingTask?.isCompleted == true) StatusFilter.COMPLETED else StatusFilter.PENDING) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) "Nueva tarea" else "Editar tarea") }
            )
        }
    ) { innerPadding ->
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
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            SimpleDropdown(
                label = "Prioridad",
                selectedLabel = priority.label,
                options = TaskPriority.entries,
                optionLabel = { it.label },
                onOptionSelected = { priority = it }
            )

            SimpleDropdown(
                label = "Estado",
                selectedLabel = status.label,
                options = listOf(StatusFilter.PENDING, StatusFilter.COMPLETED),
                optionLabel = { it.label },
                onOptionSelected = { status = it }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
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

                        viewModel.saveTask(
                            taskId = taskId,
                            title = title,
                            description = description,
                            priority = priority,
                            isCompleted = status == StatusFilter.COMPLETED
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

    LaunchedEffect(taskId, editingTask) {
        if (taskId != null && editingTask == null) {
            snackbarHostState.showSnackbar("No se encontro la tarea a editar")
            navController.popBackStack(NavigationRoutes.TASK_LIST, inclusive = false)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> SimpleDropdown(
    label: String,
    selectedLabel: String,
    options: List<T>,
    optionLabel: (T) -> String,
    onOptionSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        androidx.compose.material3.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
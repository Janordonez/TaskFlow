package com.example.taskvmg2.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taskvmg2.ui.model.Task
import com.example.taskvmg2.ui.model.TaskPriority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow(
        listOf(
            Task(
                id = 1,
                title = "Entregar informe de laboratorio",
                description = "Subir el PDF final antes de las 23:59.",
                priority = TaskPriority.HIGH,
                isCompleted = false
            ),
            Task(
                id = 2,
                title = "Repasar Kotlin para evaluación",
                description = "Practicar ViewModel, StateFlow y Navigation Compose.",
                priority = TaskPriority.MEDIUM,
                isCompleted = false
            ),
            Task(
                id = 3,
                title = "Organizar escritorio y apuntes",
                description = "Ordenar carpetas de materias y pendientes personales.",
                priority = TaskPriority.LOW,
                isCompleted = true
            )
        )
    )
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun getTaskById(taskId: Int): Task? {
        return _tasks.value.firstOrNull { it.id == taskId }
    }

    fun addTask(title: String, description: String, priority: TaskPriority) {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty()) return

        val newTask = Task(
            id = nextTaskId(),
            title = cleanTitle,
            description = description.trim(),
            priority = priority,
            isCompleted = false
        )
        _tasks.update { current -> current + newTask }
    }

    fun updateTask(taskId: Int, title: String, description: String, priority: TaskPriority) {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty()) return

        _tasks.update { current ->
            current.map { task ->
                if (task.id == taskId) {
                    task.copy(
                        title = cleanTitle,
                        description = description.trim(),
                        priority = priority
                    )
                } else {
                    task
                }
            }
        }
    }

    fun upsertTask(taskId: Int?, title: String, description: String, priority: TaskPriority) {
        if (taskId == null) {
            addTask(title, description, priority)
        } else {
            updateTask(taskId, title, description, priority)
        }
    }

    fun deleteTask(taskId: Int) {
        _tasks.update { current -> current.filterNot { it.id == taskId } }
    }

    fun toggleTaskCompletion(taskId: Int) {
        _tasks.update { current ->
            current.map { task ->
                if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
            }
        }
    }

    private fun nextTaskId(): Int {
        return (_tasks.value.maxOfOrNull { it.id } ?: 0) + 1
    }
}
package com.example.taskflow.data

import com.example.taskflow.model.Task
import com.example.taskflow.model.TaskPriority

class TaskRepository {
    private var nextId = 4

    private val tasks = mutableListOf(
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

    fun getTasks(): List<Task> {
        return tasks.toList()
    }

    fun getTask(taskId: Int): Task? {
        return tasks.firstOrNull { it.id == taskId }
    }

    fun saveTask(taskId: Int?, title: String, description: String, priority: TaskPriority, isCompleted: Boolean) {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty()) return

        val cleanDescription = description.trim()

        if (taskId == null) {
            tasks.add(
                Task(
                    id = nextId++,
                    title = cleanTitle,
                    description = cleanDescription,
                    priority = priority,
                    isCompleted = isCompleted
                )
            )
            return
        }

        val index = tasks.indexOfFirst { it.id == taskId }
        if (index == -1) return

        tasks[index] = tasks[index].copy(
            title = cleanTitle,
            description = cleanDescription,
            priority = priority,
            isCompleted = isCompleted
        )
    }

    fun deleteTask(taskId: Int) {
        tasks.removeAll { it.id == taskId }
    }

    fun toggleTaskCompletion(taskId: Int) {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index == -1) return

        val currentTask = tasks[index]
        tasks[index] = currentTask.copy(isCompleted = !currentTask.isCompleted)
    }
}
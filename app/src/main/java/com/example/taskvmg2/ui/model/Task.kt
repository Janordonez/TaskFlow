package com.example.taskvmg2.ui.model

enum class TaskPriority {
    HIGH,
    MEDIUM,
    LOW
}

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val priority: TaskPriority,
    val isCompleted: Boolean = false
)

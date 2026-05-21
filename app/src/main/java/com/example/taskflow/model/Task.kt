package com.example.taskflow.model

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val priority: TaskPriority,
    val isCompleted: Boolean = false
)

enum class TaskPriority(val label: String) {
    HIGH("Alta"),
    MEDIUM("Media"),
    LOW("Baja")
}

enum class PriorityFilter(val label: String) {
    ALL("Todas"),
    HIGH("Alta"),
    MEDIUM("Media"),
    LOW("Baja")
}

enum class StatusFilter(val label: String) {
    ALL("Todos"),
    PENDING("Pendiente"),
    COMPLETED("Completada")
}

fun Task.matchesFilters(priorityFilter: PriorityFilter, statusFilter: StatusFilter): Boolean {
    val matchesPriority = when (priorityFilter) {
        PriorityFilter.ALL -> true
        PriorityFilter.HIGH -> priority == TaskPriority.HIGH
        PriorityFilter.MEDIUM -> priority == TaskPriority.MEDIUM
        PriorityFilter.LOW -> priority == TaskPriority.LOW
    }

    val matchesStatus = when (statusFilter) {
        StatusFilter.ALL -> true
        StatusFilter.PENDING -> !isCompleted
        StatusFilter.COMPLETED -> isCompleted
    }

    return matchesPriority && matchesStatus
}
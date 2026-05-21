package com.example.taskvmg2.ui.model

enum class PriorityFilter(val label: String) {
    ALL("Todos"),
    HIGH("Alta"),
    MEDIUM("Media"),
    LOW("Baja")
}

enum class StatusFilter(val label: String) {
    ALL("Todos"),
    PENDING("Pendiente"),
    COMPLETED("Completada")
}

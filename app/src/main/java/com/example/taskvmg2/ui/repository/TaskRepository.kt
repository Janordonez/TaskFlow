package com.example.taskvmg2.ui.repository

import com.example.taskvmg2.ui.model.Task
import com.example.taskvmg2.ui.model.TaskPriority

class TaskRepository {
    private val tasks = mutableListOf<Task>(
        Task(1, "Task 1", "Descripcion de ejemplo", TaskPriority.MEDIUM, false),
        Task(2, "Task 2", "Descripcion de ejemplo", TaskPriority.HIGH, true),
        Task(3, "Task 3", "Descripcion de ejemplo", TaskPriority.LOW, false),
        Task(4, "Task 4", "Descripcion de ejemplo", TaskPriority.MEDIUM, true),
        Task(5, "Task 5", "Descripcion de ejemplo", TaskPriority.HIGH, false)
    )

    fun getTasks(): List<Task>  = tasks

    fun addTask(task: Task) = tasks.add(task)

    fun getTaskId(id: Int): Task? = tasks.find { it.id == id }

    fun removeTask(task: Task) = tasks.remove(task)

    fun toggleTask(task: Task) {
        val index = tasks.indexOf(task)
        if (index != -1) {
            tasks[index] = task.copy(isCompleted = !task.isCompleted)
        }
    }


}
package com.example.taskvmg2.ui.repository

import com.example.taskvmg2.ui.model.Task

class TaskRepository {
    private val tasks = mutableListOf<Task>(
           Task(1, "Task 1", false),
           Task(2, "Task 2", true),
           Task(3, "Task 3", false),
           Task(4, "Task 4", true),
           Task(5, "Task 5", false)
    )

    fun getTasks(): List<Task>  = tasks

    fun addTask(task: Task) = tasks.add(task)

    fun getTaskId(id: Int): Task? = tasks.find { it.id == id }

    fun removeTask(task: Task) = tasks.remove(task)

    fun toggleTask(task: Task) {
        val index = tasks.indexOf(task)
        if (index != -1) {
            tasks[index] = task.copy(completed = !task.completed)
        }
    }


}
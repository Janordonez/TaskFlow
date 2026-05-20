package com.example.taskvmg2.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskvmg2.ui.repository.TaskRepository
import com.example.taskvmg2.ui.model.Task

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()

    var tasks by mutableStateOf(listOf<Task>())
        private set

    var id by mutableStateOf("")
        private set
    var title by mutableStateOf("")
        private set
    var completed by mutableStateOf(false)
        private set


    init {
        loadTask()
    }

    fun onIdChange(newId: String) {
        this.id = newId
    }
    fun onTitleChange(newTitle: String) {
        this.title = newTitle
    }
    fun onCompletedChange(newCompleted: Boolean) {
        this.completed = newCompleted
    }

    private fun loadTask() {
        tasks = repository.getTasks()
    }
    fun loadTask(taskId: Int?) {
        if (taskId == null) {
            clearForm()
            return
        } else {
            val task = repository.getTaskId(taskId)
            task?.let {
                id = it.id.toString()
                title = it.title
                completed = it.completed
            }
        }
    }
    fun addTask(task: Task) {
        repository.addTask(task)
        loadTask()
    }
    fun removeTask(task: Task) {
        repository.removeTask(task)
        loadTask()
    }
    fun toggleTask(task: Task) {
        repository.toggleTask(task)
        loadTask()
    }
    fun getTaskId(id: Int): Task? {
        return repository.getTaskId(id)
    }
    fun clearForm(){
        id=""
        title=""
        completed=false
    }
}
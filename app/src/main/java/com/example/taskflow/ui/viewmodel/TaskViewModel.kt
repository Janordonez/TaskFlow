package com.example.taskflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taskflow.data.TaskRepository
import com.example.taskflow.model.PriorityFilter
import com.example.taskflow.model.StatusFilter
import com.example.taskflow.model.Task
import com.example.taskflow.model.TaskPriority
import com.example.taskflow.model.matchesFilters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val filteredTasks: List<Task> = emptyList(),
    val selectedPriorityFilter: PriorityFilter = PriorityFilter.ALL,
    val selectedStatusFilter: StatusFilter = StatusFilter.ALL
)

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        refreshTasks()
    }

    fun setPriorityFilter(filter: PriorityFilter) {
        _uiState.value = _uiState.value.copy(selectedPriorityFilter = filter)
        refreshTasks()
    }

    fun setStatusFilter(filter: StatusFilter) {
        _uiState.value = _uiState.value.copy(selectedStatusFilter = filter)
        refreshTasks()
    }

    fun getTaskById(taskId: Int): Task? {
        return repository.getTask(taskId)
    }

    fun saveTask(taskId: Int?, title: String, description: String, priority: TaskPriority, isCompleted: Boolean) {
        repository.saveTask(taskId, title, description, priority, isCompleted)
        refreshTasks()
    }

    fun deleteTask(taskId: Int) {
        repository.deleteTask(taskId)
        refreshTasks()
    }

    fun toggleTaskCompletion(taskId: Int) {
        repository.toggleTaskCompletion(taskId)
        refreshTasks()
    }

    private fun refreshTasks() {
        val currentTasks = repository.getTasks()
        val priorityFilter = _uiState.value.selectedPriorityFilter
        val statusFilter = _uiState.value.selectedStatusFilter

        _uiState.value = TaskUiState(
            tasks = currentTasks,
            filteredTasks = currentTasks.filter { task -> task.matchesFilters(priorityFilter, statusFilter) },
            selectedPriorityFilter = priorityFilter,
            selectedStatusFilter = statusFilter
        )
    }
}
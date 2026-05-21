package com.example.taskvmg2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskvmg2.ui.model.PriorityFilter
import com.example.taskvmg2.ui.model.StatusFilter
import com.example.taskvmg2.ui.model.Task
import com.example.taskvmg2.ui.model.TaskPriority
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class TaskListUiState(
    val filteredTasks: List<Task> = emptyList(),
    val selectedPriorityFilter: PriorityFilter = PriorityFilter.ALL,
    val selectedStatusFilter: StatusFilter = StatusFilter.ALL,
    val pendingCount: Int = 0,
    val completedCount: Int = 0
)

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

    private val _selectedPriorityFilter = MutableStateFlow(PriorityFilter.ALL)
    val selectedPriorityFilter: StateFlow<PriorityFilter> = _selectedPriorityFilter.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow(StatusFilter.ALL)
    val selectedStatusFilter: StateFlow<StatusFilter> = _selectedStatusFilter.asStateFlow()

    val taskListUiState: StateFlow<TaskListUiState> = combine(
        _tasks,
        _selectedPriorityFilter,
        _selectedStatusFilter
    ) { currentTasks, priorityFilter, statusFilter ->
        val filtered = currentTasks.filter { task ->
            matchesPriority(task, priorityFilter) && matchesStatus(task, statusFilter)
        }

        TaskListUiState(
            filteredTasks = filtered,
            selectedPriorityFilter = priorityFilter,
            selectedStatusFilter = statusFilter,
            pendingCount = filtered.count { !it.isCompleted },
            completedCount = filtered.count { it.isCompleted }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TaskListUiState(
            filteredTasks = _tasks.value,
            pendingCount = _tasks.value.count { !it.isCompleted },
            completedCount = _tasks.value.count { it.isCompleted }
        )
    )

    fun setPriorityFilter(filter: PriorityFilter) {
        _selectedPriorityFilter.value = filter
    }

    fun setStatusFilter(filter: StatusFilter) {
        _selectedStatusFilter.value = filter
    }

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

    private fun matchesPriority(task: Task, filter: PriorityFilter): Boolean {
        return when (filter) {
            PriorityFilter.ALL -> true
            PriorityFilter.HIGH -> task.priority == TaskPriority.HIGH
            PriorityFilter.MEDIUM -> task.priority == TaskPriority.MEDIUM
            PriorityFilter.LOW -> task.priority == TaskPriority.LOW
        }
    }

    private fun matchesStatus(task: Task, filter: StatusFilter): Boolean {
        return when (filter) {
            StatusFilter.ALL -> true
            StatusFilter.PENDING -> !task.isCompleted
            StatusFilter.COMPLETED -> task.isCompleted
        }
    }
}
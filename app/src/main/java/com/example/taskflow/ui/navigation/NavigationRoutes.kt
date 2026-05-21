package com.example.taskflow.ui.navigation

object NavigationRoutes {
    const val SPLASH = "splash"
    const val TASK_LIST = "task_list"
    const val ADD_EDIT_TASK = "add_edit_task"
    const val TASK_ID_ARG = "taskId"

    fun addEditRoute(taskId: Int? = null): String {
        return if (taskId == null) {
            ADD_EDIT_TASK
        } else {
            "$ADD_EDIT_TASK?$TASK_ID_ARG=$taskId"
        }
    }
}
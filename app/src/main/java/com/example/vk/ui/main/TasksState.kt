package com.example.vk.ui.main

import com.example.vk.datacontrol.Task

sealed class TasksState {
    object Loading : TasksState()
    data class Success(
        val tasks: List<Task>,
        val completedCount: Int = 0,
        val totalCount: Int = 0
    ) : TasksState()
    data class Error(val message: String) : TasksState()
}
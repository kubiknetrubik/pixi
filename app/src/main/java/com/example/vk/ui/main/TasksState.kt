package com.example.vk.ui.main

import com.example.vk.datacontrol.Task

sealed class TasksState {
    object Loading : TasksState()
    data class Success(val tasks: List<Task>) : TasksState()
    data class Error(val message: String) : TasksState()
}
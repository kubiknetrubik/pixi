package com.example.vk.datacontrol

import android.content.Context
import com.example.vk.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val cost: Int,
    val updatedAt: Long = System.currentTimeMillis()
)

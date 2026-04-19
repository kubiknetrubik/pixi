package com.example.vk.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "name_table")
data class NameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val cost: Int = 0,
    val updatedAt: Long = System.currentTimeMillis(),
    val isRewardClaimed: Boolean = false
)

package com.example.vk.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM name_table ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NameEntity>>

    @Query("SELECT * FROM name_table WHERE id = :id")
    suspend fun getNoteById(id: Int): NameEntity?

    @Insert
    suspend fun insertNote(note: NameEntity): Long

    @Update
    suspend fun updateNote(note: NameEntity)

    @Query("DELETE FROM name_table WHERE id = :id")
    suspend fun deleteNote(id: Int)
}

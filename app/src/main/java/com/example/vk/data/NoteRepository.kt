package com.example.vk.data
import android.util.Log
import com.example.vk.datacontrol.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(
    private val noteDao: Dao,
    private val balanceRepository: BalanceRepository) {
    fun getAllNotesAsTasks(): Flow<List<Task>> = noteDao.getAllNotes().map { notes ->
        notes.map { it.toTask() }
    }

    suspend fun insertNote(note: NameEntity): Long = noteDao.insertNote(note)

    suspend fun toggleNoteCompleted(id: Int) {
        val note = noteDao.getNoteById(id) ?: return
        val newStatus = !note.isCompleted

        if (newStatus && !note.isRewardClaimed) {
            balanceRepository.addBalance(note.cost)

            Log.d("Balance", "Начислено: ${note.cost}")

            noteDao.updateNote(
                note.copy(
                    isCompleted = true,
                    isRewardClaimed = true,
                    updatedAt = System.currentTimeMillis()
                )
            )
        } else {
            noteDao.updateNote(
                note.copy(
                    isCompleted = newStatus,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun deleteNote(id: Int) = noteDao.deleteNote(id)

    suspend fun updateNote(id: Int, title: String, description: String, cost: Int) {
        noteDao.getNoteById(id)?.let { note ->
            noteDao.updateNote(
                note.copy(
                    title = title.trim(),
                    description = description.trim(),
                    cost = cost,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}



private fun NameEntity.toTask() = Task(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    cost = cost,
    updatedAt = updatedAt
    )
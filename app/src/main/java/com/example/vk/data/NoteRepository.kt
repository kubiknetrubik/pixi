package com.example.vk.data
import com.example.vk.datacontrol.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(private val noteDao: Dao) {

    fun getAllNotesAsTasks(): Flow<List<Task>> = noteDao.getAllNotes().map { notes ->
        notes.map { it.toTask() }
    }

    suspend fun insertNote(note: NameEntity): Long = noteDao.insertNote(note)

    suspend fun deleteNote(id: Int) = noteDao.deleteNote(id)

    suspend fun toggleNoteCompleted(id: Int) {
        noteDao.getNoteById(id)?.let { note ->
            noteDao.updateNote(note.copy(isCompleted = !note.isCompleted, updatedAt = System.currentTimeMillis()))
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
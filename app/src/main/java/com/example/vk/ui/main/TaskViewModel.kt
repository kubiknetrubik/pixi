package com.example.vk.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk.data.NameEntity
import com.example.vk.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TaskViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<TasksState>(TasksState.Loading)
    val uiState: StateFlow<TasksState> = _uiState

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        viewModelScope.launch {
            noteRepository.getAllNotesAsTasks()
                .catch { e ->
                    _uiState.value = TasksState.Error(e.message ?: "Ошибка загрузки")
                }
                .collect { tasks ->
                    _uiState.value = TasksState.Success(tasks)
                }
        }
    }

    fun addNote(title: String, description: String, cost: Int = 0) {
        if (title.isBlank()) {
            _snackbarMessage.value = "Нельзя сохранить пустую заметку"
            return
        }
        viewModelScope.launch {
            noteRepository.insertNote(
                NameEntity(
                    title = title.trim(),
                    description = description.trim(),
                    cost = cost
                )
            )
        }
    }

    fun toggleNoteCompleted(id: Int) {
        viewModelScope.launch {
            noteRepository.toggleNoteCompleted(id)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            noteRepository.deleteNote(id)
        }
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}



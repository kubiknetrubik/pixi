package com.example.vk.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk.data.BalanceRepository
import com.example.vk.data.NameEntity
import com.example.vk.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TaskViewModel(
    private val noteRepository: NoteRepository,
    private val balanceRepository: BalanceRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow<TasksState>(TasksState.Loading)
    val uiState: StateFlow<TasksState> = _uiState

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    val balance: StateFlow<Int> = balanceRepository.balance

    init {
        viewModelScope.launch {
            noteRepository.getAllNotesAsTasks()
                .catch { e ->
                    _uiState.value = TasksState.Error(e.message ?: "Ошибка загрузки")
                }
                .collect { tasks ->
                    val completed = tasks.count { it.isCompleted }
                    val total = tasks.size
                    _uiState.value = TasksState.Success(tasks, completed, total)
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
    fun updateNote(id: Int, title: String, description: String, cost: Int) {
        viewModelScope.launch {
            noteRepository.updateNote(id, title, description, cost)
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


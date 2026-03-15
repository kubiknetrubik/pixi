package com.example.vk.ui.general

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GeneralViewModel : ViewModel() {
    private val _uistate = MutableStateFlow(GeneralUIState())
    val uiState : StateFlow<GeneralUIState> = _uistate.asStateFlow()

    fun selectedLamb(){
        _uistate.value = _uistate.value.copy(selectedPets = SelectedPet.LAMB)
    }
    fun selectedCat(){
        _uistate.value = _uistate.value.copy(selectedPets = SelectedPet.CAT)
    }
}
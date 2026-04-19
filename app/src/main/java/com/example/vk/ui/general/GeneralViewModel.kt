package com.example.vk.ui.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk.data.BalanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GeneralViewModel(
    private val balanceRepository: BalanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GeneralUIState())
    val uiState: StateFlow<GeneralUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            balanceRepository.balance.collect { balance ->
                _uiState.update { it.copy(coins = balance) }
            }
        }
    }
    //
    fun selectLamb() {
        _uiState.value = _uiState.value.copy(selectedPets = SelectedPet.LAMB)
        SelectedPetHolder.setSelected(SelectedPet.LAMB)
    }

    fun selectCat() {
        _uiState.value = _uiState.value.copy(selectedPets = SelectedPet.CAT)
        SelectedPetHolder.setSelected(SelectedPet.CAT)
    }

    fun purchaseLamb() {
        val currentState = _uiState.value
        if (currentState.coins >= 50) {
            repeat(50) { balanceRepository.addBalance(-1) }
            _uiState.value = currentState.copy(
                selectedPets = SelectedPet.LAMB,
                coins = balanceRepository.balance.value
            )
            SelectedPetHolder.setSelected(SelectedPet.LAMB)
        }
    }

    fun purchaseCat() {
        val currentState = _uiState.value
        if (currentState.coins >= 100) {
            repeat(100) { balanceRepository.addBalance(-1) }
            _uiState.value = currentState.copy(selectedPets = SelectedPet.CAT,
                coins = balanceRepository.balance.value
            )

            SelectedPetHolder.setSelected(SelectedPet.CAT)
        }
    }



}

package com.example.vk.ui.general

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SelectedPetHolder {
    private val _selected = MutableStateFlow(SelectedPet.LAMB)
    val selected: StateFlow<SelectedPet> = _selected.asStateFlow()

    fun setSelected(pet: SelectedPet) {
        _selected.value = pet
    }
}

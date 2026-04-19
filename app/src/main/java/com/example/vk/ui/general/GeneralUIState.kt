package com.example.vk.ui.general

data class GeneralUIState(
    val selectedPets : SelectedPet = SelectedPet.LAMB,
    val coins: Int = 0
)
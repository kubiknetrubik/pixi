package com.example.vk.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BalanceRepository(context: Context) {
    private val prefs = context.getSharedPreferences("balance", Context.MODE_PRIVATE)
    private val _balance = MutableStateFlow(prefs.getInt("balance", 0))
    val balance: StateFlow<Int> = _balance

    fun addBalance(amount: Int) {
        val new = _balance.value + amount
        _balance.value = new
        prefs.edit().putInt("balance", new).apply()
    }
}
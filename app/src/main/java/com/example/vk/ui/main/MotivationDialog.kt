package com.example.vk.ui.main

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.vk.data.Quotes
import com.example.vk.ui.theme.OrangeContinue

@Composable
fun MotivationDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = OrangeContinue,
        title = { Text("✨ Вдохновение ✨", color = Color.White) },
        text = {
            Text(
                text = "\"${Quotes.getTodaysQuote()}\"",
                fontSize = 16.sp,
                color = Color.White
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Продолжить", color = Color.White)
            }
        }
    )
}
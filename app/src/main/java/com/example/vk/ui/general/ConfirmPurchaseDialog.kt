package com.example.vk.ui.general

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.vk.ui.theme.OrangeContinue
import com.example.vk.ui.theme.SignupBackground

@Composable
fun ConfirmPurchaseDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = (SignupBackground),
        title = { Text("Подтверждение покупки", color = Color.Black) },
        text = { Text("Купить персонажа?", color = Color.Black) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Да", color = OrangeContinue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Нет", color = OrangeContinue)
            }
        }
    )
}
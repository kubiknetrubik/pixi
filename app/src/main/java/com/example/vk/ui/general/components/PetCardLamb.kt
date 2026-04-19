package com.example.vk.ui.general.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vk.R


@Composable
fun PetCardLamb(
    isSelected: Boolean,
    coins: Int,
    onSelect: () -> Unit,
    onPurchase: () -> Unit,
    onShowConfirmDialog: (() -> Unit) -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val charactersText = stringResource(R.string.characters)
    val lambDesc = stringResource(R.string.lamb)
    val selectedText = stringResource(R.string.selected)
    val notEnoughCoinsText = stringResource(R.string.not_enough_coins_lamb)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GeneralChip(text = charactersText, onClick = {})
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(93.dp)
                .clickable(
                    onClick = onSelect,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = Color.White, radius = 20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_barash),
                contentDescription = lambDesc,
                modifier = Modifier.size(93.dp, 74.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        PetButton(
            text = if (isSelected) selectedText else "50",
            selected = isSelected,
            currencyIcon = !isSelected,
            onClick = {
                if (isSelected) {
                    onSelect()
                } else {
                    if (coins >= 50) {
                        onShowConfirmDialog(onPurchase)
                    } else {
                        onShowToast(notEnoughCoinsText)
                    }
                }
            }
        )
    }
}
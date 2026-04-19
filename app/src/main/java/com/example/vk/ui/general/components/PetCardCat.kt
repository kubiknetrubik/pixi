package com.example.vk.ui.general.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vk.R

@Composable
fun PetCardCat(
    isSelected: Boolean,
    coins: Int,
    onSelect: () -> Unit,
    onPurchase: () -> Unit,
    onShowConfirmDialog: (() -> Unit) -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val clothesText = stringResource(R.string.clothes)
    val catDesc = stringResource(R.string.cat)
    val selectedText = stringResource(R.string.selected)
    val notEnoughCoinsText = stringResource(R.string.not_enough_coins_cat)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GeneralChip(text = clothesText, onClick = {})
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(93.dp)
                .clickable { onSelect() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_cat),
                contentDescription = catDesc,
                modifier = Modifier.size(93.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        PetButton(
            text = if (isSelected) selectedText else "100",
            selected = isSelected,
            currencyIcon = !isSelected,
            onClick = {
                if (isSelected) {
                    onSelect()
                } else {
                    if (coins >= 100) {
                        onShowConfirmDialog(onPurchase)
                    } else {
                        onShowToast(notEnoughCoinsText)
                    }
                }
            }
        )
    }
}
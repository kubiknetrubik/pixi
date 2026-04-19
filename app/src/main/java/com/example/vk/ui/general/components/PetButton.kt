package com.example.vk.ui.general.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vk.R
import com.example.vk.ui.theme.OrangeContinue

private val PetButtonOrangeBg = Color(0x4DF5A315)

@Composable
fun PetButton(
    text: String,
    selected: Boolean,
    currencyIcon: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(63.dp)
            .height(23.dp)
            .clickable(onClick = onClick)
            .background(
                color = if (selected) Color.White else PetButtonOrangeBg,
                shape = RoundedCornerShape(12.dp)
            )
            .then(
                if (selected)
                    Modifier.border(
                        width = 2.dp,
                        color = OrangeContinue,
                        shape = RoundedCornerShape(12.dp)
                    )
                else Modifier
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontSize = 10.sp,
                color = Color.Black,
            )
            if (currencyIcon) {
                Spacer(modifier = Modifier.width(2.dp))
                Image(
                    painter = painterResource(id = R.drawable.money),
                    contentDescription = null,
                    modifier = Modifier
                        .width(25.dp)
                        .height(37.dp)
                )
            }
        }
    }
}
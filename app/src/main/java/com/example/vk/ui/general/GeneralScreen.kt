package com.example.vk.ui.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vk.R
import com.example.vk.ui.components.bars.BottomBar
import com.example.vk.ui.theme.OrangeContinue
import com.example.vk.ui.theme.SignupBackground

private val PetButtonOrangeBg = Color(0x4DF5A315)

@Composable
fun GeneralScreen(
    viewModel: GeneralViewModel = viewModel(),
    onNavigatetoSettings: () -> Unit = {},
    onNavigatetoTasks: () -> Unit = {},
    onNavigatetoShop: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedPet = uiState.selectedPets

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(SignupBackground)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pixi),
                    contentDescription = "Pixi logo",
                    modifier = Modifier.size(80.dp, 32.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "150",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Image(
                        painter = painterResource(id = R.drawable.money),
                        contentDescription = "Currency",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.diamond),
                    contentDescription = "Diamond",
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 4.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = if (selectedPet == SelectedPet.LAMB) R.drawable.barash else R.drawable.cat
                    ),
                    contentDescription = if (selectedPet == SelectedPet.LAMB) "Lamb" else "Cat",
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .size(364.dp, 321.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GeneralChip(text = "Персонажи", onClick = {})
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .height(93.dp)
                                .clickable { viewModel.selectedLamb() },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_barash),
                                contentDescription = "Lamb",
                                modifier = Modifier.size(93.dp, 74.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        PetButton(
                            text = if (selectedPet == SelectedPet.LAMB) "Выбрано" else "50",
                            selected = selectedPet == SelectedPet.LAMB,
                            currencyIcon = selectedPet != SelectedPet.LAMB,
                            onClick = { viewModel.selectedLamb() }
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GeneralChip(text = "Одежда", onClick = {})
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .size(93.dp)
                                .clickable { viewModel.selectedCat() },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_cat),
                                contentDescription = "Cat",
                                modifier = Modifier.size(93.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        PetButton(
                            text = if (selectedPet == SelectedPet.CAT) "Выбрано" else "100",
                            selected = selectedPet == SelectedPet.CAT,
                            currencyIcon = selectedPet != SelectedPet.CAT,
                            onClick = { viewModel.selectedCat() }
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GeneralChip(text = "Аксессуары", onClick = {})
                    }
                }
            }
        }


        BottomBar(
            onNavigatetoSettings = onNavigatetoSettings,
            onNavigatetoTasks = onNavigatetoTasks,
            onNavigatetoShop = onNavigatetoShop
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GeneralScreenPreview() {
    GeneralScreen()
}

@Composable
private fun PetButton(
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
            modifier = Modifier
                .fillMaxHeight(),
            //.padding(bottom = 2.dp),
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
                    modifier = Modifier.width(25.dp)
                        .height(37.dp)
                )
            }
        }
    }
}

@Composable
private fun GeneralChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(110.dp)
            .height(35.dp)
            .clickable(onClick = onClick)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 2.dp,
                color = OrangeContinue,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

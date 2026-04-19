package com.example.vk.ui.general

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vk.R
import com.example.vk.ui.components.bars.BottomBar
import com.example.vk.ui.general.components.GeneralChip
import com.example.vk.ui.general.components.PetCardCat
import com.example.vk.ui.general.components.PetCardLamb
import com.example.vk.ui.general.components.ShopTopBar
import com.example.vk.ui.theme.SignupBackground
import kotlinx.coroutines.delay

@Composable
fun GeneralScreen(
    viewModel: GeneralViewModel,
    onNavigatetoSettings: () -> Unit = {},
    onNavigatetoTasks: () -> Unit = {},
    onNavigatetoShop: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedPet = uiState.selectedPets
    val context = LocalContext.current
    var showConfirmDialog by remember { mutableStateOf(false) }
    var pendingPurchase by remember { mutableStateOf<(() -> Unit)?>(null) }

    LaunchedEffect(Unit) {
        delay(5000)
        println("Баланс в магазине: ${uiState.coins}")
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showConfirmDialogForPurchase(purchase: () -> Unit) {
        pendingPurchase = purchase
        showConfirmDialog = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(SignupBackground)
    ) {

        ShopTopBar(coins = uiState.coins)

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

                AnimatedPetImage(selectedPet = selectedPet)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {

                    PetCardLamb(
                        isSelected = selectedPet == SelectedPet.LAMB,
                        coins = uiState.coins,
                        onSelect = { viewModel.selectLamb() },
                        onPurchase = { viewModel.purchaseLamb() },
                        onShowConfirmDialog = ::showConfirmDialogForPurchase,
                        onShowToast = ::showToast
                    )


                    PetCardCat(
                        isSelected = selectedPet == SelectedPet.CAT,
                        coins = uiState.coins,
                        onSelect = { viewModel.selectCat() },
                        onPurchase = { viewModel.purchaseCat() },
                        onShowConfirmDialog = ::showConfirmDialogForPurchase,
                        onShowToast = ::showToast
                    )


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GeneralChip(
                            text = stringResource(R.string.accessories),
                            onClick = { }
                        )
                    }
                }
            }
        }

        BottomBar(
            onNavigatetoSettings = onNavigatetoSettings,
            onNavigatetoTasks = onNavigatetoTasks,
            onNavigatetoShop = onNavigatetoShop
        )

        if (showConfirmDialog) {
            ConfirmPurchaseDialog(
                onConfirm = {
                    pendingPurchase?.invoke()
                    showConfirmDialog = false
                    pendingPurchase = null
                },
                onDismiss = {
                    showConfirmDialog = false
                    pendingPurchase = null
                }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedPetImage(
    selectedPet: SelectedPet,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = selectedPet,
        transitionSpec = {
            fadeIn() with fadeOut() using SizeTransform(clip = true)
        }
    ) { pet ->
        Image(
            painter = painterResource(
                id = if (pet == SelectedPet.LAMB) R.drawable.barash else R.drawable.cat
            ),
            contentDescription = if (pet == SelectedPet.LAMB) stringResource(R.string.lamb) else stringResource(R.string.cat),
            modifier = modifier.size(364.dp, 321.dp)
        )
    }
}

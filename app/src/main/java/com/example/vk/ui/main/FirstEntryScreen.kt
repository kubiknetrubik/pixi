package com.example.vk.ui.main

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vk.R
import com.example.vk.datacontrol.Task
import com.example.vk.ui.components.bars.BottomBar
import com.example.vk.ui.general.SelectedPet
import com.example.vk.ui.general.SelectedPetHolder
import com.example.vk.ui.main.components.TopBarSection
import com.example.vk.ui.main.components.TasksListSection
import com.example.vk.ui.theme.OrangePrimary
import com.example.vk.ui.theme.SignupBackground

@Composable
fun FirstEntryScreen(
    vm: TaskViewModel,
    onNavigatetoSettings: () -> Unit = {},
    onNavigatetoShop: () -> Unit = {}
) {
    val state by vm.uiState.collectAsState()
    val snackbarMessage by vm.snackbarMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val balance by vm.balance.collectAsState()

    var showAddNoteDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var showMotivationDialog by remember { mutableStateOf(false) }

    val completedCount = (state as? TasksState.Success)?.completedCount ?: 0
    val totalCount = (state as? TasksState.Success)?.totalCount ?: 0
    val selectedPet by SelectedPetHolder.selected.collectAsState(initial = SelectedPet.LAMB)
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(Unit) {
        showMotivationDialog = true
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            vm.clearSnackbar()
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        if (isLandscape) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopBarSection(balance, completedCount, totalCount)

                    PetSection(
                        selectedPet = selectedPet,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 16.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    BottomBar(
                        onNavigatetoSettings = onNavigatetoSettings,
                        onNavigatetoShop = onNavigatetoShop
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    FloatingActionButton(
                        onClick = { showAddNoteDialog = true },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(24.dp),
                        containerColor = OrangePrimary,
                        contentColor = Color.White
                    ) {
                        Text("+", fontSize = 24.sp)
                    }
                }

                Spacer(modifier = Modifier.width(150.dp))

                Box(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .height(500.dp)
                        .width(380.dp)
                ) {
                    TasksListSection(
                        state = state,
                        onToggle = vm::toggleNoteCompleted,
                        onDelete = vm::deleteNote,
                        onEdit = { editingTask = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SignupBackground),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBarSection(balance, completedCount, totalCount)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        PetSection(
                            selectedPet = selectedPet,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        TasksListSection(
                            state = state,
                            onToggle = vm::toggleNoteCompleted,
                            onDelete = vm::deleteNote,
                            onEdit = { editingTask = it },
                            modifier = Modifier
                                .height(280.dp)
                                .width(380.dp)
                        )
                    }
                }

                FloatingActionButton(
                    onClick = { showAddNoteDialog = true },
                    modifier = Modifier.padding(top = 16.dp),
                    containerColor = OrangePrimary,
                    contentColor = Color.White
                ) {
                    Text("+", fontSize = 24.sp)
                }

                BottomBar(
                    onNavigatetoSettings = onNavigatetoSettings,
                    onNavigatetoShop = onNavigatetoShop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }


    if (showAddNoteDialog) {
        AddNoteDialog(
            onDismiss = { showAddNoteDialog = false },
            onSave = { title, description, cost ->
                vm.addNote(title, description, cost)
                showAddNoteDialog = false
            }
        )
    }

    if (editingTask != null) {
        AddNoteDialog(
            onDismiss = { editingTask = null },
            onSave = { title, description, cost ->
                vm.updateNote(editingTask!!.id, title, description, cost)
                editingTask = null
            }
        )
    }

    if (showMotivationDialog) {
        MotivationDialog(
            onDismiss = { showMotivationDialog = false }
        )
    }
}


@Composable
fun PetSection(
    selectedPet: SelectedPet,
    modifier: Modifier = Modifier
) {
    val petDrawable = if (selectedPet == SelectedPet.LAMB) R.drawable.barash else R.drawable.cat
    val petContentDescription = if (selectedPet == SelectedPet.LAMB) "Lamb" else "Cat"

    Image(
        painter = painterResource(id = petDrawable),
        contentDescription = petContentDescription,
        modifier = modifier.size(364.dp, 321.dp)
    )
}
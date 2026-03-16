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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vk.R
import com.example.vk.ui.components.bars.BottomBar
import com.example.vk.ui.general.SelectedPet
import com.example.vk.ui.general.SelectedPetHolder
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
    var showAddNoteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            vm.clearSnackbar()
        }
    }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val selectedPet by SelectedPetHolder.selected.collectAsState(initial = SelectedPet.LAMB)

    val petDrawable = if (selectedPet == SelectedPet.LAMB) R.drawable.barash else R.drawable.cat
    val petContentDescription = if (selectedPet == SelectedPet.LAMB) "Lamb" else "Cat"

    if (isLandscape) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = petDrawable),
                        contentDescription = petContentDescription,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 16.dp)
                            .size(364.dp, 321.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BottomBar(
                        onNavigatetoSettings = onNavigatetoSettings,
                        onNavigatetoShop = onNavigatetoShop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Spacer(modifier = Modifier.width(150.dp))
                Box(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .height(500.dp)
                        .width(380.dp)
                ) {
                    when (state) {
                        is TasksState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        is TasksState.Success -> {
                            val tasks = (state as TasksState.Success).tasks
                            if (tasks.isEmpty()) {
                                Text(
                                    text = "Нет заметок. Создайте первую.",
                                    modifier = Modifier.align(Alignment.Center),
                                    color = colorResource(R.color.error)
                                )
                            } else {
                                Spacer(modifier = Modifier.height(20.dp))
                                LazyColumn(
                                    modifier = Modifier
                                        .height(500.dp)
                                        .width(380.dp)
                                ) {
                                    items(tasks) { task ->
                                        TaskItem(
                                            task = task,
                                            onToggle = { vm.toggleNoteCompleted(task.id) },
                                            onDelete = { vm.deleteNote(task.id) }
                                        )
                                    }
                                }
                            }
                        }
                        is TasksState.Error -> {
                            Text(
                                text = (state as TasksState.Error).message,
                                modifier = Modifier.align(Alignment.Center),
                                color = colorResource(R.color.error)
                            )
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = { showAddNoteDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.dp),
                containerColor = OrangePrimary,
                contentColor = Color.White
            ) {
                Text("+", fontSize = 24.sp)
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SignupBackground),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                        Image(
                            painter = painterResource(id = petDrawable),
                            contentDescription = petContentDescription,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .size(364.dp, 321.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Box(
                            modifier = Modifier
                                .height(280.dp)
                                .width(380.dp)
                        ) {
                            when (state) {
                                is TasksState.Loading -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                is TasksState.Success -> {
                                    val tasks = (state as TasksState.Success).tasks
                                    if (tasks.isEmpty()) {
                                        Text(
                                            text = "Нет заметок. Создайте первую.",
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(16.dp),
                                            color = colorResource(R.color.error)
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.height(20.dp))
                                        LazyColumn(
                                            modifier = Modifier
                                                .height(270.dp)
                                                .width(380.dp)
                                        ) {
                                            items(tasks) { task ->
                                                TaskItem(
                                                    task = task,
                                                    onToggle = { vm.toggleNoteCompleted(task.id) },
                                                    onDelete = { vm.deleteNote(task.id) }
                                                )
                                            }
                                        }
                                    }
                                }
                                is TasksState.Error -> {
                                    Text(
                                        text = (state as TasksState.Error).message,
                                        modifier = Modifier.align(Alignment.Center),
                                        color = colorResource(R.color.error)
                                    )
                                }
                            }
                        }
                    }
                }
                BottomBar(
                    onNavigatetoSettings = onNavigatetoSettings,
                    onNavigatetoShop = onNavigatetoShop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            FloatingActionButton(
                onClick = { showAddNoteDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.dp),
                containerColor = OrangePrimary,
                contentColor = Color.White
            ) {
                Text("+", fontSize = 24.sp)
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
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
}
package com.example.vk.ui.main

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vk.R
import com.example.vk.ui.components.bars.BottomBar
import com.example.vk.ui.theme.SignupBackground
import com.example.vk.data.NameEntity
import com.example.vk.data.NoteRepository
import com.example.vk.datacontrol.Task
import com.example.vk.ui.theme.OrangeContinue
import com.example.vk.ui.theme.OrangePrimary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class TasksState {
    object Loading : TasksState()
    data class Success(val tasks: List<Task>) : TasksState()
    data class Error(val message: String) : TasksState()
}

class TaskViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<TasksState>(TasksState.Loading)
    val uiState: StateFlow<TasksState> = _uiState

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        viewModelScope.launch {
            noteRepository.getAllNotesAsTasks()
                .catch { e ->
                    _uiState.value = TasksState.Error(e.message ?: "Ошибка загрузки")
                }
                .collect { tasks ->
                    _uiState.value = TasksState.Success(tasks)
                }
        }
    }
    fun addNote(title: String, description: String, cost: Int = 0) {
        if (title.isBlank()) {
            _snackbarMessage.value = "Нельзя сохранить пустую заметку"
            return
        }
        viewModelScope.launch {
            noteRepository.insertNote(
                NameEntity(
                    title = title.trim(),
                    description = description.trim(),
                    cost = cost
                )
            )
        }
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
@Composable
fun TaskItem(task:Task){
    val bc = if (task.isCompleted) colorResource(R.color.accent) else colorResource(R.color.white)
    Row(
        modifier= Modifier
            .border(2.dp,OrangePrimary, RoundedCornerShape(10.dp))
            .height(53.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .height(23.dp)
                .width(24.dp)
                .border(2.dp,colorResource(R.color.accent), RoundedCornerShape(5.dp))
                .background(bc,shape = RoundedCornerShape(5.dp))

        ){}
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = task.title,
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = task.cost.toString(),
            fontSize = 15.sp
        )
        Icon(
            painter = painterResource(id = R.drawable.dust),
            contentDescription = stringResource(R.string.dust),
            modifier = Modifier
                .height(37.dp)
                .width(25.dp)
        )

    }


    Spacer(modifier = Modifier.height(10.dp))

}

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
    if (isLandscape) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(){
                    Image(
                        painter = painterResource(id = R.drawable.main_fox),
                        contentDescription = null,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    BottomBar(onNavigatetoSettings = onNavigatetoSettings, onNavigatetoShop = onNavigatetoShop)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Spacer(modifier = Modifier.width(150.dp))
                Box(
                    modifier = Modifier
                        .height(500.dp)
                        .width(380.dp)
                ) {
                    when(state){
                        is TasksState.Loading ->{
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
                                        TaskItem(task = task)
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.main_fox),
                    contentDescription = stringResource(R.string.fox),
                    modifier = Modifier.size(380.dp, 346.dp)
                )
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
                                        TaskItem(task = task)
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
                BottomBar(onNavigatetoSettings = onNavigatetoSettings, onNavigatetoShop = onNavigatetoShop)
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
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var reward by remember { mutableStateOf(50f) }
        Dialog(onDismissRequest = { showAddNoteDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(2.dp, OrangeContinue)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Новая заметка",
                        color = Color.Black,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Заголовок", color = Color.Black) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = OrangeContinue,
                            unfocusedBorderColor = OrangeContinue,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Описание", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = OrangeContinue,
                            unfocusedBorderColor = OrangeContinue,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Награда за выполнение задания",
                        color = Color.Black,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.money),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Slider(
                            value = reward,
                            onValueChange = { reward = it },
                            valueRange = 1f..100f,
                            steps = 98,
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(
                                thumbColor = OrangePrimary,
                                activeTrackColor = OrangeContinue,
                                inactiveTrackColor = OrangeContinue.copy(alpha = 0.3f)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${reward.toInt()}",
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.width(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showAddNoteDialog = false }) {
                            Text("Отмена", color = Color.Black)
                        }
                        TextButton(
                            onClick = {
                                vm.addNote(title, description, reward.toInt())
                                showAddNoteDialog = false
                            }
                        ) {
                            Text("Сохранить", color = OrangePrimary)
                        }
                    }
                }
            }
        }
    }
}
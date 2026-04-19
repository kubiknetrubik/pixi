package com.example.vk.ui.main.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vk.R
import com.example.vk.datacontrol.Task
import com.example.vk.ui.main.TaskItem
import com.example.vk.ui.main.TasksState

@Composable
fun TasksListSection(
    state: TasksState,
    onToggle: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (state) {
            is TasksState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is TasksState.Success -> {
                val tasks = state.tasks
                if (tasks.isEmpty()) {
                    Text(
                        text = stringResource(R.string.empty_tasks),
                        modifier = Modifier.align(Alignment.Center),
                        color = colorResource(R.color.error)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(tasks) { task ->
                            TaskItem(
                                task = task,
                                onToggle = { onToggle(task.id) },
                                onDelete = { onDelete(task.id) },
                                onEdit = { onEdit(task) }
                            )
                        }
                    }
                }
            }
            is TasksState.Error -> {
                Text(
                    text = state.message,
                    modifier = Modifier.align(Alignment.Center),
                    color = colorResource(R.color.error)
                )
            }
        }
    }
}
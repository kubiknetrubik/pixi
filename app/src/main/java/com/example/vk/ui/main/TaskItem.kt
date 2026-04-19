package com.example.vk.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vk.R
import com.example.vk.datacontrol.Task
import com.example.vk.ui.theme.OrangePrimary

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var showDescriptionDialog by remember { mutableStateOf(value=false) }

    Row(
        modifier = Modifier
            .border(2.dp, OrangePrimary, RoundedCornerShape(10.dp))
            .height(53.dp)
            .fillMaxWidth()
            .clickable { onEdit() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = OrangePrimary,
                uncheckedColor = OrangePrimary
            )
        )
        if (task.description.isNotBlank()) {
            Text(
                text = "📄",
                fontSize = 16.sp,
                modifier = Modifier.clickable { showDescriptionDialog = true }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = task.title,
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = task.cost.toString(),
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.width(4.dp))

        Image(
            painter = painterResource(R.drawable.money),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = "Удалить",
            modifier = Modifier
                .size(28.dp)
                .clickable(onClick = onDelete),
            colorFilter = ColorFilter.tint(OrangePrimary)
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
    Spacer(modifier = Modifier.height(10.dp))

    if (showDescriptionDialog) {
        DescriptionDialog(
            title = task.title,
            description = task.description,
            cost = task.cost,
            onDismiss = { showDescriptionDialog = false }
        )
    }
}

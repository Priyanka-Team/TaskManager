package com.bornfire.taskmanagement.ui.screen

import com.bornfire.taskmanagement.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bornfire.taskmanagement.data.local.entity.TaskEntity
import com.bornfire.taskmanagement.utils.CategorySelector
import com.bornfire.taskmanagement.utils.DateTimePickerIcon
import com.bornfire.taskmanagement.utils.PrioritySelector
import com.bornfire.taskmanagement.viewmodel.CategoryViewModel
import com.bornfire.taskmanagement.viewmodel.TaskViewModel
@Composable
fun TaskDetailActivity(
    navController: NavHostController,
    taskId: String,
    viewModel: TaskViewModel = hiltViewModel()
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var editTitle by remember { mutableStateOf("") }
    var editDescription by remember { mutableStateOf("") }
    var taskToEdit by remember { mutableStateOf<TaskEntity?>(null) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showPrioritySelector by remember { mutableStateOf(false) }
    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val task = viewModel.selectedTask.value

    LaunchedEffect(taskId) {
        viewModel.getTaskById(taskId)
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?\n\nTask title: ${task?.title}") },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF8687E7), shape = RoundedCornerShape(8.dp))
                        .clickable {
                            viewModel.deleteTask(task?.id.toString())
                            showDeleteDialog = false
                            navController.popBackStack()
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                Box(
                    modifier = Modifier
                        .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
                        .clickable { showDeleteDialog = false }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Cancel", color = Color(0xFF8687E7))
                }
            }
        )
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Task Title") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        placeholder = { Text(editTitle) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editDescription,
                        onValueChange = { editDescription = it },
                        placeholder = { Text(editDescription) }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        taskToEdit?.let {
                            val updatedTask = it.copy(
                                title = editTitle,
                                description = editDescription
                            )
                            viewModel.updateTask(updatedTask)
                        }
                        showEditDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        if (task != null) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Close Button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                navController.navigate("home")
                            }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Title & Edit
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = task.title,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                taskToEdit = task
                                editTitle = task.title
                                editDescription = task.description
                                showEditDialog = true
                            }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = task.description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                RowItem(
                    iconResId = R.drawable.ic_time,
                    label = "Task Time",
                    value = task.dateTime,
                    onClick = { showTimePicker = true }
                )

                RowItem(
                    iconResId = R.drawable.ic_category,
                    label = "Task Category",
                    value = task.category,
                    onClick = { showCategoryDialog = true }
                )

                RowItem(
                    iconResId = R.drawable.ic_priority,
                    label = "Task Priority",
                    value = task.priority.toString(),
                    onClick = { showPrioritySelector = true }
                )

                RowItem(
                    iconResId = R.drawable.ic_subtask,
                    label = "Sub-Task",
                    value = "Add Sub-Task",
                    onClick = {  }
                )
                if (showTimePicker) {
                    DateTimePickerIcon(
                        onDateTimeSelected = { selected ->
                            viewModel.updateTaskTime(task.id.toString(), selected)
                            showTimePicker = false
                        }
                    )
                }

                CategorySelector(
                    showDialog = showCategoryDialog,
                    onDismiss = { showCategoryDialog = false },
                    onCategorySelected = { category ->
                        viewModel.updateTaskCategory(task.id.toString(), category)
                        showCategoryDialog = false
                    },
                    viewModel = categoryViewModel
                )

                PrioritySelector(
                    showPopup = showPrioritySelector,
                    onDismiss = { showPrioritySelector = false },
                    onSave = { priority ->
                        viewModel.updateTaskPriority(task.id.toString(), priority)
                        showPrioritySelector = false
                    }
                )

                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable { showDeleteDialog = true }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "Delete",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Delete Task",
                        color = Color.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8687E7))
                ) {
                    Text(text = "Edit Task", color = Color.White, fontSize = 16.sp)
                }
            }
        } else {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun RowItem(
    iconResId: Int,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .background(Color.DarkGray)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

    }
}

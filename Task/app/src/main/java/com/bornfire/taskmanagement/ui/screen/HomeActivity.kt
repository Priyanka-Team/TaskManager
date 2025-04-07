package com.bornfire.taskmanagement.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bornfire.taskmanagement.R
import androidx.compose.runtime.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import com.bornfire.taskmanagement.utils.CategorySelector
import com.bornfire.taskmanagement.utils.DateTimePickerIcon
import com.bornfire.taskmanagement.utils.PrioritySelector
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bornfire.taskmanagement.viewmodel.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.bornfire.taskmanagement.data.local.entity.TaskEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeActivity(navController: NavHostController) {
    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val taskViewModel: TaskViewModel = hiltViewModel()
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val taskList by taskViewModel.taskList.collectAsState(initial = emptyList())
    var selectedDateTime by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(0) }
    var selectedTaskId by remember { mutableStateOf<Int?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filterOptions = listOf("All", "Today")

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", Locale.getDefault())
    val today = LocalDate.now()

    val filteredTasks = taskList.filter {
        val matchesSearch = it.title.contains(searchQuery, ignoreCase = true)

        val matchesDate = when (selectedFilter) {
            "Today" -> {
                val taskDate = try {
                    val dateTime = LocalDateTime.parse(it.dateTime, formatter)
                    dateTime.toLocalDate()
                } catch (e: Exception) {
                    null
                }
                taskDate == today
            }
            else -> true
        }

        matchesSearch && matchesDate
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp),
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        containerColor = Color.Black,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.Black)
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Task Manager",
                    fontSize = 24.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))
                if (filteredTasks.isNotEmpty()) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search for your Task", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.White
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                var expanded by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.35f) // Takes 25% of the width
                    ) {
                        OutlinedTextField(
                            value = selectedFilter,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("All", color = Color.White) },
                            trailingIcon = {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_dropdown),
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color.Gray,
                            )
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        filterOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedFilter = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
                if (filteredTasks.isEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_list_bg),
                        contentDescription = "Center Image",
                        modifier = Modifier.size(227.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No tasks found",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(filteredTasks) { task ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                            ) {
                                Row(modifier = Modifier.padding(16.dp)) {
                                    // Left side RadioButton
                                    RadioButton(
                                        selected = selectedTaskId == task.id,
                                        onClick = {
                                            selectedTaskId = task.id
                                            navController.navigate("detail/${task.id}")
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color.White,
                                            unselectedColor = Color.Gray
                                        )
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    // Right side content
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = task.title,
                                            fontSize = 18.sp,
                                            color = Color.White
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))



                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {

                                            Text(
                                                text = "Date: ${task.dateTime}",
                                                fontSize = 12.sp,
                                                color = Color.LightGray
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            // Category box with icon and name
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .background(Color(0xFF4CAF50), shape = RoundedCornerShape(8.dp))
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_category),
                                                    contentDescription = "Category",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = task.category,
                                                    fontSize = 12.sp,
                                                    color = Color.White
                                                )
                                            }

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .border(1.dp,Color.Blue,RectangleShape)
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_priority),
                                                    contentDescription = "Priority",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = task.priority.toString(),
                                                    fontSize = 12.sp,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {},
                    containerColor = Color(0xFF363636),
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Add Task",
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Start)

                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                placeholder = { Text("Title", color = Color.White) },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.Gray,
                                    focusedIndicatorColor = Color.White,
                                    unfocusedIndicatorColor = Color.Gray,
                                )
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                placeholder = { Text("Description", color = Color.White) },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.Gray,
                                    focusedIndicatorColor = Color.White,
                                    unfocusedIndicatorColor = Color.Gray,
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    DateTimePickerIcon(
                                        onDateTimeSelected = { dateTime ->
                                            selectedDateTime = dateTime
                                        }
                                    )

                                    var showCategoryDialog by remember { mutableStateOf(false) }

                                    IconButton(onClick = { showCategoryDialog = true }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_category),
                                            contentDescription = "Category",
                                            tint = Color.White
                                        )
                                    }

                                    CategorySelector(
                                        showDialog = showCategoryDialog,
                                        onDismiss = { showCategoryDialog = false },
                                        onCategorySelected = { category ->
                                            selectedCategory = category
                                        },
                                        viewModel = categoryViewModel
                                    )

                                    var showPrioritySelector by remember { mutableStateOf(false) }

                                    IconButton(onClick = {
                                        showPrioritySelector = true
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_priority),
                                            contentDescription = "Priority",
                                            tint = Color.White
                                        )
                                    }

                                    PrioritySelector(
                                        showPopup = showPrioritySelector,
                                        onDismiss = { showPrioritySelector = false },
                                        onSave = { priority ->
                                            selectedPriority = priority
                                        }
                                    )

                                }
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    val task = TaskEntity(
                                        title = title,
                                        description = description,
                                        dateTime = selectedDateTime,
                                        category = selectedCategory,
                                        priority = selectedPriority
                                    )
                                    taskViewModel.addTask(task)
                                    showDialog = false
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_save),
                                        contentDescription = "Confirm",
                                        tint = Color.Blue
                                    )
                                }

                            }

                        }
                    }
                )
            }
        }
    )
}



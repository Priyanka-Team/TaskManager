package com.bornfire.taskmanagement.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bornfire.taskmanagement.R

@Composable
fun PrioritySelector(
    showPopup: Boolean,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    if (showPopup) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {},
            containerColor = Color(0xFF2C2C2C),
            tonalElevation = 3.dp,
            text = {
                var selectedPriority by remember { mutableStateOf(1) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Task Priority",
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))

                    val priorityList = (1..10).toList()

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(priorityList) { value ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp)
                                    .background(
                                        color = if (selectedPriority == value) Color(0xFF8687E7) else Color.Transparent,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .clickable { selectedPriority = value }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_priority),
                                        contentDescription = "Priority $value",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Text(
                                        text = "$value",
                                        color =  Color.White,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onDismiss,
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text(
                                text="Cancel",
                                color =  Color(0xFF8687E7)
                            )
                        }

                        Button(
                            onClick = {
                                onSave(selectedPriority)
                                onDismiss()
                            },
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor =  Color(0xFF8687E7))
                        ) {
                            Text(
                                text = "Save",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        )
    }
}

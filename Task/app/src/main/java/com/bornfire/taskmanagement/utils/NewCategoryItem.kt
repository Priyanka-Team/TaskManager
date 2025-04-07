package com.bornfire.taskmanagement.utils
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bornfire.taskmanagement.R
import com.bornfire.taskmanagement.viewmodel.CategoryViewModel
import com.bornfire.taskmanagement.data.local.entity.Category

@Composable
fun NewCategoryItem(
    showPopup: Boolean,
    onDismiss: () -> Unit,
    onCreate: (name: String, iconRes: Int, color: Color) -> Unit,
    categoryViewModel: CategoryViewModel
) {
    if (!showPopup) return

    val iconList = listOf(
        R.drawable.ic_home, R.drawable.ic_grocery, R.drawable.ic_work,
        R.drawable.ic_sport,R.drawable.ic_design,R.drawable.ic_university,
        R.drawable.ic_social,R.drawable.ic_music,R.drawable.ic_health,
        R.drawable.ic_movie
    )
    val colorList = listOf(
        Color(0xFFC9CC41), Color(0xFF66CC41), Color(0xFF41CCA7),
        Color(0xFF4181CC), Color(0xFF41A2CC), Color(0xFFCC8441),
        Color(0xFF9741CC), Color(0xFFCC4173), Color(0xFFFF9680),
        Color(0xFF80FFFF), Color(0xFF80FFD9), Color(0xFF809CFF),
        Color(0xFFFF80EB), Color(0xFFFFCC80)
    )

    var selectedIcon by remember { mutableStateOf(iconList.first()) }
    var selectedColor by remember { mutableStateOf(colorList.first()) }
    var categoryName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        containerColor = Color(0xFF2C2C2C),
        title = {
                Text(
                    text = "Create New Category",
                    color = Color.White,
                    fontSize = 20.sp
                )
        },
        text = {
            Column {
                Text(
                    "Category Name: ",
                    color = Color.White,
                    fontSize = 14.sp
                )
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    placeholder = { Text("category name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true
                )

                Text(
                    "Category Icon",
                    color = Color.White,
                    fontSize = 14.sp
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(iconList.size) { index ->
                        val icon = iconList[index]
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (selectedIcon == icon) Color.DarkGray else Color.Gray
                                )
                                .clickable { selectedIcon = icon },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = "icon",
                                tint = Color.White
                            )
                        }
                    }
                }

                Text(
                    "Category Color",
                    color = Color.White,
                    fontSize = 14.sp
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(colorList.size) { index ->
                        val color = colorList[index]
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = 2.dp,
                                    color = if (color == selectedColor) Color.White else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text="Cancel",
                            color=Color(0xFF8687E7))
                    }

                    Button(
                        onClick = {
                            if (categoryName.isNotBlank()) {
                                val category = Category(
                                    name = categoryName.trim(),
                                    icon = selectedIcon,
                                    color = selectedColor.value.toLong()
                                )
                                categoryViewModel.insertCategory(category)
                                onDismiss()
                            }
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8687E7))
                    ) {
                        Text(
                            text="Create Category",
                            color = Color.White)
                    }
                }
            }
        }
    )
}

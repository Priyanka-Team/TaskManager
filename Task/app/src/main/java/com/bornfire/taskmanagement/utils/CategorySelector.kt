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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bornfire.taskmanagement.R
import com.bornfire.taskmanagement.viewmodel.CategoryViewModel

@Composable
fun CategorySelector(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onCategorySelected: (String) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    var showNewCategoryPopup by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {},
            containerColor = Color(0xFF2C2C2C),
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Select Category", fontSize = 20.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.Gray)
                }
            },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(440.dp)
                ) {
                    items(categories.size + 1) { index ->
                        if (index < categories.size) {
                            val category = categories[index]
                            CategoryItem(
                                categoryName = category.name,
                                backgroundColor = Color(category.color.toULong()),
                                iconResId = category.icon
                            ) {
                                onCategorySelected(category.name)
                                onDismiss()
                            }
                        } else {
                            CategoryItem(
                                categoryName = "Add New",
                                backgroundColor = Color.Blue,
                                iconResId = R.drawable.ic_new_add
                            ) {
                                showNewCategoryPopup = true
                            }
                        }
                    }

                }
            }
        )
    }

    if (showNewCategoryPopup) {
        NewCategoryItem(
            showPopup = true,
            onDismiss = { showNewCategoryPopup = false },
            onCreate = { name, iconRes, color ->
                viewModel.addCategory(name, iconRes, color.value.toLong())
                showNewCategoryPopup = false
            },
            viewModel,
        )
    }
}
@Composable
fun CategoryItem(
    categoryName: String,
    backgroundColor: Color,
    iconResId: Int? = null,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(100.dp)
    ) {
        // Box with colored background and icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (iconResId != null) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = categoryName,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_category),
                    contentDescription = "Default Icon",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category name below the box
        Text(
            text = categoryName,
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

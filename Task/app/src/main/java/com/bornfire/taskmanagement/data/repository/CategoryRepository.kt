package com.bornfire.taskmanagement.data.repository

import com.bornfire.taskmanagement.data.local.dao.CategoryDao
import com.bornfire.taskmanagement.data.local.entity.Category
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val dao: CategoryDao
) {
    val allCategories = dao.getAllCategories() // Assuming it returns a Flow<List<Category>>

    suspend fun insert(category: Category) = dao.insert(category)
}

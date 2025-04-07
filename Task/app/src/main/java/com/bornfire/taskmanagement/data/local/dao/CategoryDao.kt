package com.bornfire.taskmanagement.data.local.dao

import androidx.room.*
import androidx.room.OnConflictStrategy
import com.bornfire.taskmanagement.data.local.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>
}
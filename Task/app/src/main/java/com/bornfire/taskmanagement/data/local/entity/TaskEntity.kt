package com.bornfire.taskmanagement.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dateTime: String,
    val category: String,
    val priority: Int,
    val isDeleted: Boolean = false
)

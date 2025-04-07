package com.bornfire.taskmanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bornfire.taskmanagement.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM task_table WHERE isDeleted = 0 ORDER BY id DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): TaskEntity?

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("UPDATE task_table SET isDeleted = 1 WHERE id = :taskId")
    suspend fun deleteTask(taskId: String)
}

package com.bornfire.taskmanagement.data.repository

import com.bornfire.taskmanagement.data.local.dao.TaskDao
import com.bornfire.taskmanagement.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    suspend fun insertTask(task: TaskEntity) = taskDao.insertTask(task)
    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()
    suspend fun getTaskById(taskId: String): TaskEntity? = taskDao.getTaskById(taskId)
    suspend fun deleteTask(taskId: String) = taskDao.deleteTask(taskId)
    suspend fun updateTask(task: TaskEntity)=taskDao.updateTask(task)
}


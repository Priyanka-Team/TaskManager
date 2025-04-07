package com.bornfire.taskmanagement.viewmodel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornfire.taskmanagement.data.local.entity.TaskEntity
import com.bornfire.taskmanagement.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.Flow


@HiltViewModel
class TaskViewModel @Inject constructor(val repository: TaskRepository) : ViewModel() {
    val taskList: Flow<List<TaskEntity>> = repository.getAllTasks()
    private val _selectedTask = mutableStateOf<TaskEntity?>(null)
    val selectedTask: State<TaskEntity?> = _selectedTask

    fun getTaskById(taskId: String) {
        viewModelScope.launch {
            _selectedTask.value = repository.getTaskById(taskId)
        }
    }

    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }
    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }
    fun updateTaskTime(taskId: String, newTime: String) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            task?.let {
                val updatedTask = it.copy(dateTime = newTime)
                repository.updateTask(updatedTask)
                _selectedTask.value = updatedTask
            }
        }
    }

    fun updateTaskCategory(taskId: String, newCategory: String) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            task?.let {
                val updatedTask = it.copy(category = newCategory)
                repository.updateTask(updatedTask)
                _selectedTask.value = updatedTask
            }
        }
    }

    fun updateTaskPriority(taskId: String, newPriority: Int) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            task?.let {
                val updatedTask = it.copy(priority = newPriority)
                repository.updateTask(updatedTask)
                _selectedTask.value = updatedTask
            }
        }
    }


}


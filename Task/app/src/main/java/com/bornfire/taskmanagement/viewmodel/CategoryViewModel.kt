package com.bornfire.taskmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornfire.taskmanagement.data.local.entity.Category
import com.bornfire.taskmanagement.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    val categories = repository.allCategories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addCategory(name: String, icon: Int, color: Long) {
        viewModelScope.launch {
            repository.insert(Category(name = name, icon = icon, color = color))
        }
    }

    fun insertCategory(category: Category) {
        viewModelScope.launch {
            repository.insert(category)
        }
    }
}

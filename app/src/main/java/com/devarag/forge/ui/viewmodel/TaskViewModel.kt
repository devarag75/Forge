package com.devarag.forge.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devarag.forge.data.database.DatabaseProvider
import com.devarag.forge.data.entity.Task
import com.devarag.forge.data.repository.ForgeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ForgeRepository

    val tasks: StateFlow<List<Task>>

    init {

        val db = DatabaseProvider.getDatabase(application)

        repository = ForgeRepository(
            db.taskDao(),
            db.focusSessionDao()
        )

        tasks = repository.tasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            repository.addTask(Task(title = title))
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
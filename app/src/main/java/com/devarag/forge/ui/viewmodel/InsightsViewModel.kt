
package com.devarag.forge.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devarag.forge.data.database.DatabaseProvider
import com.devarag.forge.data.entity.FocusSession
import com.devarag.forge.data.entity.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class InsightsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = DatabaseProvider.getDatabase(application)

    val tasks: StateFlow<List<Task>> =
        database.taskDao().getAllTasks().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val focusSessions: StateFlow<List<FocusSession>> =
        database.focusSessionDao().getAllSessions().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}

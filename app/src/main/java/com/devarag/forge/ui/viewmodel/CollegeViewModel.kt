
package com.devarag.forge.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devarag.forge.data.database.DatabaseProvider
import com.devarag.forge.data.entity.Absence
import com.devarag.forge.data.entity.DutyLeave
import com.devarag.forge.data.repository.CollegeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CollegeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = DatabaseProvider.getDatabase(application)

    private val repository = CollegeRepository(
        database.absenceDao(),
        database.dutyLeaveDao()
    )

    val absences: StateFlow<List<Absence>> =
        repository.absences.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val dutyLeaves: StateFlow<List<DutyLeave>> =
        repository.dutyLeaves.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addAbsence(absence: Absence) {
        viewModelScope.launch {
            repository.addAbsence(absence)
        }
    }

    fun deleteAbsence(absence: Absence) {
        viewModelScope.launch {
            repository.deleteAbsence(absence)
        }
    }

    fun addDutyLeave(dutyLeave: DutyLeave) {
        viewModelScope.launch {
            repository.addDutyLeave(dutyLeave)
        }
    }

    fun deleteDutyLeave(dutyLeave: DutyLeave) {
        viewModelScope.launch {
            repository.deleteDutyLeave(dutyLeave)
        }
    }

    fun restoreBackup(
        restoredAbsences: List<Absence>,
        restoredDutyLeaves: List<DutyLeave>
    ) {
        viewModelScope.launch {
            restoredAbsences.forEach {
                repository.addAbsence(it)
            }

            restoredDutyLeaves.forEach {
                repository.addDutyLeave(it)
            }
        }
    }
}

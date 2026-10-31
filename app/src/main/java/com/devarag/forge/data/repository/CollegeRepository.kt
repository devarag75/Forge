package com.devarag.forge.data.repository

import com.devarag.forge.data.dao.AbsenceDao
import com.devarag.forge.data.dao.DutyLeaveDao
import com.devarag.forge.data.entity.Absence
import com.devarag.forge.data.entity.DutyLeave

class CollegeRepository(
    private val absenceDao: AbsenceDao,
    private val dutyLeaveDao: DutyLeaveDao
) {

    val absences = absenceDao.getAllAbsences()

    val dutyLeaves = dutyLeaveDao.getAllDutyLeaves()

    suspend fun addAbsence(absence: Absence) {
        absenceDao.insertAbsence(absence)
    }

    suspend fun updateAbsence(absence: Absence) {
        absenceDao.updateAbsence(absence)
    }

    suspend fun deleteAbsence(absence: Absence) {
        absenceDao.deleteAbsence(absence)
    }

    suspend fun addDutyLeave(dutyLeave: DutyLeave) {
        dutyLeaveDao.insertDutyLeave(dutyLeave)
    }

    suspend fun updateDutyLeave(dutyLeave: DutyLeave) {
        dutyLeaveDao.updateDutyLeave(dutyLeave)
    }

    suspend fun deleteDutyLeave(dutyLeave: DutyLeave) {
        dutyLeaveDao.deleteDutyLeave(dutyLeave)
    }
}
package com.devarag.forge.data.dao

import androidx.room.*
import com.devarag.forge.data.entity.Absence
import kotlinx.coroutines.flow.Flow

@Dao
interface AbsenceDao {

    @Query("SELECT * FROM absences ORDER BY date DESC")
    fun getAllAbsences(): Flow<List<Absence>>

    @Query("SELECT * FROM absences WHERE id = :id")
    suspend fun getAbsenceById(id: Int): Absence?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAbsence(absence: Absence)

    @Update
    suspend fun updateAbsence(absence: Absence)

    @Delete
    suspend fun deleteAbsence(absence: Absence)
}
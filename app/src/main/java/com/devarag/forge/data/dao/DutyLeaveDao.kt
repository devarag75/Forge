package com.devarag.forge.data.dao

import androidx.room.*
import com.devarag.forge.data.entity.DutyLeave
import kotlinx.coroutines.flow.Flow

@Dao
interface DutyLeaveDao {

    @Query("SELECT * FROM duty_leaves ORDER BY startDate DESC")
    fun getAllDutyLeaves(): Flow<List<DutyLeave>>

    @Query("SELECT * FROM duty_leaves WHERE id = :id")
    suspend fun getDutyLeaveById(id: Int): DutyLeave?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDutyLeave(dutyLeave: DutyLeave)

    @Update
    suspend fun updateDutyLeave(dutyLeave: DutyLeave)

    @Delete
    suspend fun deleteDutyLeave(dutyLeave: DutyLeave)
}
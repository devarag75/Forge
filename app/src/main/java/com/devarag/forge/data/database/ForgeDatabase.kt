
package com.devarag.forge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devarag.forge.data.dao.AbsenceDao
import com.devarag.forge.data.dao.DutyLeaveDao
import com.devarag.forge.data.dao.FocusSessionDao
import com.devarag.forge.data.dao.NoteDao
import com.devarag.forge.data.dao.TaskDao
import com.devarag.forge.data.entity.Absence
import com.devarag.forge.data.entity.DutyLeave
import com.devarag.forge.data.entity.FocusSession
import com.devarag.forge.data.entity.Note
import com.devarag.forge.data.entity.Task

@Database(
    entities = [
        Task::class,
        FocusSession::class,
        Absence::class,
        DutyLeave::class,
        Note::class
    ],
    version = 3,
    exportSchema = false
)
abstract class ForgeDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun focusSessionDao(): FocusSessionDao

    abstract fun absenceDao(): AbsenceDao

    abstract fun dutyLeaveDao(): DutyLeaveDao

    abstract fun noteDao(): NoteDao
}
